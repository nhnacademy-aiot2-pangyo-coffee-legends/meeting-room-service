package com.nhnacademy.meetingroomservice.controller;

import com.nhnacademy.meetingroomservice.dto.*;
import com.nhnacademy.meetingroomservice.service.MeetingRoomService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 회의실과 관련된 작업 API endpoints를 관리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/v1/meeting-rooms") // prefix: /api/v1, RouteLocator Bean을 직접설정해서 Spring Cloud Gateway에서 url prefix 경로를 지정해주는 방법도 생각해볼 것.
@RequiredArgsConstructor
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    /**
     *
     * @param meetingRoomRegisterRequest 회의실 등록 요청 DTO
     * @return 신규 등록된 회의실의 이름과 수용인원을 담은 DTO 객체 반환
     */
    @PostMapping("/register")
    public ResponseEntity<MeetingRoomResponse> registerMeetingRoom(@RequestBody MeetingRoomRegisterRequest meetingRoomRegisterRequest) {

        MeetingRoomResponse meetingRoomResponse = meetingRoomService.registerMeetingRoom(
                meetingRoomRegisterRequest.getMeetingRoomName(),
                meetingRoomRegisterRequest.getMeetingRoomCapacity(),
                meetingRoomRegisterRequest.getEquipmentIds()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(meetingRoomResponse);
    }

    /**
     *
     * @param no 회의실 번호
     * @return 회의실 이름과 수용인원에 대한 정보를 담고 있는 DTO 반환
     */
    @GetMapping("/{meeting-room-id}")
    public ResponseEntity<MeetingRoomResponse> getMeetingRoom(@PathVariable("meeting-room-id") Long no) {
        MeetingRoomResponse meetingRoomResponse = meetingRoomService.getMeetingRoom(no);

        return ResponseEntity
                .ok(meetingRoomResponse);
    }


    /**
     *
     * @return 회의실 DTO 리스트 반환
     */
    @GetMapping
    public ResponseEntity<List<MeetingRoomResponse>> getMeetingRoomList() {
        List<MeetingRoomResponse> meetingRoomResponseList = meetingRoomService.getMeetingRoomList();

        return ResponseEntity
                .ok(meetingRoomResponseList);
    }

    /**
     *
     * @param no 회의실 번호
     * @param meetingRoomUpdateRequest 회의실 정보 업데이트 항목을 담고 있는 DTO
     * @return 업데이트 된 정보를 들고 있는 회의실 DTO 반환
     */
    @PutMapping("/{meeting-room-id}")
    public ResponseEntity<MeetingRoomResponse> updateMeetingRoom(@PathVariable("meeting-room-id") Long no, @RequestBody MeetingRoomUpdateRequest meetingRoomUpdateRequest) {
        MeetingRoomResponse meetingRoomResponse = meetingRoomService.updateMeetingRoom(
                no,
                meetingRoomUpdateRequest.getMeetingRoomName(),
                meetingRoomUpdateRequest.getMeetingRoomCapacity(),
                meetingRoomUpdateRequest.getEquipmentIds()
        );

        return ResponseEntity
                .ok(meetingRoomResponse);
    }

    /**
     *
     * @param no 회의실 번호
     * @return 회의실 삭제 성공 여부 반환
     */
    @DeleteMapping("/{meeting-room-id}")
    public ResponseEntity<Void> deleteMeetingRoom(@PathVariable("meeting-room-id") Long no) {

        meetingRoomService.deleteMeetingRoom(no);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    /**
     *
     * @param entryRequest 회의실 입실 request DTO
     * @return 회의실 입실 검증 성공 시 EntryResponse DTO 반환
     */
    @PostMapping("/verify")
    public ResponseEntity<EntryResponse> enterMeetingRoom(@RequestBody EntryRequest entryRequest) {

        EntryResponse entryResponse = meetingRoomService.enterMeetingRoom(entryRequest.getCode(), entryRequest.getEntryTime(), entryRequest.getBookingNo());

        return ResponseEntity
                .ok(entryResponse);
    }
}
