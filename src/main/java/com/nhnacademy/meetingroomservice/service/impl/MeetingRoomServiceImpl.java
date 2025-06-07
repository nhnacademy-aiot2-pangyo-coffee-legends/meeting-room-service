package com.nhnacademy.meetingroomservice.service.impl;

import com.nhnacademy.meetingroomservice.adaptor.BookingAdaptor;
import com.nhnacademy.meetingroomservice.domain.Equipment;
import com.nhnacademy.meetingroomservice.domain.MeetingRoom;
import com.nhnacademy.meetingroomservice.dto.EntryRequest;
import com.nhnacademy.meetingroomservice.dto.EntryResponse;
import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomAlreadyExistsException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomDoesNotExistException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomNotFoundException;
import com.nhnacademy.meetingroomservice.repository.EquipmentRepository;
import com.nhnacademy.meetingroomservice.repository.MeetingRoomRepository;
import com.nhnacademy.meetingroomservice.service.MeetingRoomService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * {@link MeetingRoomService} 인터페이스를 구현한 클래스입니다.
 * 이 클래스는 회의실 생성, 조회, 수정, 삭제 등 회의실 관련 주요 비즈니스 로직을 담당합니다.
 *
 * 주요 기능:
 * <ul>
 *     <li>새로운 회의실 생성</li>
 *     <li>회의실 ID를 통한 세부 정보 조회</li>
 *     <li>회의실 이름을 기반으로 속성 수정</li>
 *     <li>회의실 ID를 통한 삭제</li>
 * </ul>
 *
 * 이 서비스는 {@link MeetingRoomRepository}를 통해 데이터베이스와 상호작용하며,
 * 외부에 반환할 응답 객체를 생성할 때 엔티티를 DTO로 변환합니다.
 * 또한 트랜잭션 관리를 보장하고, 회의실 관련 비즈니스 규칙을 강제합니다.
 *
 * 회의실이 존재하지 않거나 접근할 수 없는 경우에는
 * {@link MeetingRoomNotFoundException}, {@link MeetingRoomDoesNotExistException}과 같은 예외를 발생시킵니다.
 *
 * 이 클래스는 Spring의 {@code @Service} 어노테이션이 붙어 있어 서비스 컴포넌트로 인식되며,
 * {@code @Transactional} 어노테이션을 통해 메소드 실행 시 트랜잭션 일관성을 보장합니다.
 * 또한 Lombok의 {@code @RequiredArgsConstructor}를 사용하여
 * 필수 의존성 주입을 위한 생성자가 자동으로 생성됩니다.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class MeetingRoomServiceImpl implements MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;
    private final EquipmentRepository equipmentRepository;
    private final BookingAdaptor bookingAdaptor;

    /**
     *
     * @param meetingRoomName 회의실 이름
     * @param meetingRoomCapacity 회의실 수용인원
     * @return 생성된 회의실 Entity를 DTO로 변환하여 반환
     */
    @Override
    public MeetingRoomResponse registerMeetingRoom(String meetingRoomName, int meetingRoomCapacity, List<Long> equipmentIds) {

        List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity, equipments);

        if (meetingRoomRepository.existsMeetingRoomByMeetingRoomName(meetingRoomName)) {
            throw new MeetingRoomAlreadyExistsException(meetingRoomName);
        }

        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        return convertToMeetingRoomResponse(savedMeetingRoom);
    }

    /**
     *
     * @param no 회의실 번호
     * @return 데이터베이스에 존재하는 회의실의 정보를 DTO로 변환하여 반환
     */
    @Override
    @Transactional(readOnly = true)
    public MeetingRoomResponse getMeetingRoom(Long no) {

        MeetingRoom meetingRoom = meetingRoomRepository.findById(no).orElseThrow(() -> new MeetingRoomNotFoundException(no));

        return convertToMeetingRoomResponse(meetingRoom);
    }

    /**
     *
     * @return 테이터베이스에 존재하는 모든 회의실의 정보를 DTO로 변환하여 List로 반환
     */
    @Override
    @Transactional(readOnly = true)
    public List<MeetingRoomResponse> getMeetingRoomList() {

        List<MeetingRoom> meetingRoomList = meetingRoomRepository.findAll();

        return meetingRoomList.stream().map(
                meetingRoom -> {
                    return convertToMeetingRoomResponse(meetingRoom);
                }
        ).toList();

    }

    /**
     *
     * @param meetingRoomName 회의실 이름
     * @param meetingRoomCapacity 회의실 수용인원
     * @return 데이터베이스에 존재하는 회의실의 이름 / 수용인원을 변경하여 저장 및 DTO로 변환하여 반환
     */
    @Override
    public MeetingRoomResponse updateMeetingRoom(Long no, String meetingRoomName, int meetingRoomCapacity, List<Long> equipmentIds) {

        List<Equipment> equipments = equipmentRepository.findAllById(equipmentIds);

        MeetingRoom meetingRoom = meetingRoomRepository.findById(no).orElseThrow(() -> new MeetingRoomDoesNotExistException(no));

        meetingRoom.update(meetingRoomName, meetingRoomCapacity, equipments);

        return convertToMeetingRoomResponse(meetingRoom);
    }

    /**
     *
     * @param no 회의실 번호
     */
    @Override
    public void deleteMeetingRoom(Long no) {

        MeetingRoom meetingRoom = meetingRoomRepository.findById(no).orElseThrow(() -> new MeetingRoomDoesNotExistException(no));

        meetingRoomRepository.delete(meetingRoom);
    }

    /**
     * 회의실 입실 요청을 예약 서비스(booking service)로 전달하고, 응답을 반환합니다.
     *
     * <p>예약 정보가 유효하지 않거나 시간이 맞지 않는 경우,
     * {@link com.nhnacademy.meetingroomservice.advice.MeetingRoomControllerAdvice}에서
     * {@link feign.FeignException} 을 처리하여 JSON 형태의 오류 응답을 반환합니다.
     *
     * <p>이 메서드에서는 예외를 직접 처리하지 않습니다.
     *
     * @param code 회의실 예약 시 발급된 코드
     * @param entryTime 입실 시도 시간
     * @param bookingNo 예약 번호
     * @return 입실 응답 DTO
     * @throws FeignException 예약 서비스 호출 실패 시 발생
     */
    @Override
    public EntryResponse enterMeetingRoom(String code, LocalDateTime entryTime, Long bookingNo) {
        EntryRequest entryRequest = new EntryRequest(
                code,
                entryTime,
                bookingNo
        );

        ResponseEntity<EntryResponse> entryResponseEntity = bookingAdaptor.checkBooking(entryRequest);

        return entryResponseEntity.getBody();
    }

    /**
     *
     * @param meetingRoom 회의실 Entity 객체
     * @return 회의실 정보가 담긴 DTO로 변환하여 반환
     */
    private MeetingRoomResponse convertToMeetingRoomResponse(MeetingRoom meetingRoom) {
        List<String> equipmentNames = meetingRoom.getEquipments()
                .stream()
                .map(equipment -> equipment.getName()).toList();

        return new MeetingRoomResponse(
                meetingRoom.getNo(),
                meetingRoom.getMeetingRoomName(),
                meetingRoom.getMeetingRoomCapacity(),
                equipmentNames
        );
    }
}
