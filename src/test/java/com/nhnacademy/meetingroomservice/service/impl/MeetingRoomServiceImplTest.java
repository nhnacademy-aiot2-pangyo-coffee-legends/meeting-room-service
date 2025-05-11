package com.nhnacademy.meetingroomservice.service.impl;

import com.nhnacademy.meetingroomservice.adaptor.BookingAdaptor;
import com.nhnacademy.meetingroomservice.domain.MeetingRoom;
import com.nhnacademy.meetingroomservice.dto.EntryRequest;
import com.nhnacademy.meetingroomservice.dto.EntryResponse;
import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;
import com.nhnacademy.meetingroomservice.exception.BookingNotFoundException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomAlreadyExistsException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomDoesNotExistException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomNotFoundException;
import com.nhnacademy.meetingroomservice.repository.MeetingRoomRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
class MeetingRoomServiceImplTest {

    @Mock
    MeetingRoomRepository meetingRoomRepository;

    @Mock
    BookingAdaptor bookingAdaptor;

    @InjectMocks // Mock 객체 생성 후 자동 주입 (bean injection)
    MeetingRoomServiceImpl meetingRoomService;

    @Test
    @DisplayName("회의실 등록")
    void RegisterMeetingRoom() {
        // Given
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        when(meetingRoomRepository.existsMeetingRoomByMeetingRoomName(meetingRoomName)).thenReturn(false);

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);
        when(meetingRoomRepository.save(Mockito.any(MeetingRoom.class))).thenReturn(meetingRoom);

        // When
        MeetingRoomResponse response = meetingRoomService.registerMeetingRoom(meetingRoomName, meetingRoomCapacity);

        // Then
        assertNotNull(response);
        assertEquals(meetingRoomName, response.getMeetingRoomName());
        assertEquals(meetingRoomCapacity, response.getMeetingRoomCapacity());

    }

    @Test
    @DisplayName("이미 존재하는 회의실 이름으로 등록 시 예외 발생")
    void testRegisterMeetingRoomAlreadyExists() {
        // Given
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 20;

        when(meetingRoomRepository.existsMeetingRoomByMeetingRoomName(meetingRoomName)).thenReturn(true);

        // When / Then
        assertThrows(MeetingRoomAlreadyExistsException.class, () -> {
            meetingRoomService.registerMeetingRoom(meetingRoomName, meetingRoomCapacity);
        });
    }

    @Test
    @DisplayName("회의실 조회")
    void getMeetingRoom() {
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);

        ReflectionTestUtils.setField(meetingRoom, "no", 1L);

        when(meetingRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(meetingRoom));

        MeetingRoomResponse response = meetingRoomService.getMeetingRoom(1L);

        assertEquals(meetingRoomName, response.getMeetingRoomName());
        assertEquals(meetingRoomCapacity, response.getMeetingRoomCapacity());
    }

    @Test
    @DisplayName("존재하지 않는 회의실 조회: 실패")
    void testGetMeetingRoom() {
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);
        ReflectionTestUtils.setField(meetingRoom, "no", 1L);

        when(meetingRoomRepository.findById(1L)).thenReturn(Optional.of(meetingRoom));

        assertThrows(MeetingRoomNotFoundException.class, () -> {
            meetingRoomService.getMeetingRoom(2L);
        });
    }

    @Test
    @DisplayName("모든 회의실 조회")
    void getMeetingRoomList() {
        String meetingRoomName = "회의실 test1";
        int meetingRoomCapacity = 10;

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);
        ReflectionTestUtils.setField(meetingRoom, "no", 1L);

        String meetingRoomName2 = "회의실 test2";
        int meetingRoomCapacity2 = 15;

        MeetingRoom meetingRoom2 = MeetingRoom.ofNewMeetingRoom(meetingRoomName2, meetingRoomCapacity2);
        ReflectionTestUtils.setField(meetingRoom2, "no", 2L);

        List<MeetingRoom> meetingRoomList = List.of(meetingRoom, meetingRoom2);
        when(meetingRoomRepository.findAll()).thenReturn(meetingRoomList);

        List<MeetingRoomResponse> meetingRoomResponseList = meetingRoomService.getMeetingRoomList();

        assertEquals(2, meetingRoomResponseList.size());

        for (int i = 0; i < meetingRoomList.size(); i++) {
            MeetingRoom expected = meetingRoomList.get(i);
            MeetingRoomResponse actual = meetingRoomResponseList.get(i);

            assertEquals(expected.getNo(), actual.getNo());
            assertEquals(expected.getMeetingRoomName(), actual.getMeetingRoomName());
            assertEquals(expected.getMeetingRoomCapacity(), actual.getMeetingRoomCapacity());
        }

    }

    @Test
    @DisplayName("회의실이 존재하지 않는 경우 빈 List 반환")
    void getEmptyMeetingRoomList() {

        when(meetingRoomRepository.findAll()).thenReturn(Collections.emptyList());

        List<MeetingRoomResponse> responseList = meetingRoomService.getMeetingRoomList();

        assertNotNull(responseList);
        assertTrue(responseList.isEmpty());
    }

    @Test
    @DisplayName("회의실 업데이트")
    void updateMeetingRoom() {

        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        when(meetingRoomRepository.existsById(1L)).thenReturn(true);

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);
        ReflectionTestUtils.setField(meetingRoom, "no", 1L);

        when(meetingRoomRepository.findById(1L)).thenReturn(Optional.of(meetingRoom));

        MeetingRoomResponse response = meetingRoomService.updateMeetingRoom(1L, "회의실 test2", 15);

        assertEquals(1L, response.getNo());
        assertEquals("회의실 test2", response.getMeetingRoomName());
        assertEquals(15, response.getMeetingRoomCapacity());
    }

    @Test
    @DisplayName("존재하지 않는 회의실 업데이트 시도")
    void updateNonExistentMeetingRoom() {
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);
        ReflectionTestUtils.setField(meetingRoom, "no", 1L);

        when(meetingRoomRepository.existsById(2L)).thenReturn(false);

        assertThrows(MeetingRoomDoesNotExistException.class, () -> {
            meetingRoomService.updateMeetingRoom(2L, meetingRoomName, meetingRoomCapacity);
        });
    }

    @Test
    @DisplayName("회의실 삭제")
    void deleteMeetingRoom() {
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(meetingRoomName, meetingRoomCapacity);
        ReflectionTestUtils.setField(meetingRoom, "no", 1L);

        when(meetingRoomRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(meetingRoom));

        meetingRoomService.deleteMeetingRoom(1L);

        verify(meetingRoomRepository, times(1)).delete(meetingRoom);

    }

    @Test
    @DisplayName("존재하지 않는 회의실 삭제")
    void deleteNonExistentMeetingRoom() {

        Long nonExistentId = 1L;

        when(meetingRoomRepository.existsById(nonExistentId)).thenReturn(false);

        assertThrows(MeetingRoomDoesNotExistException.class, () -> {
            meetingRoomService.deleteMeetingRoom(nonExistentId);
        });
    }

    @Test
    @DisplayName("회의실 입실 성공")
    void enterMeetingRoom() {
        String code = "ABC34F21";
        LocalDateTime entryTime = LocalDateTime.now();
        Long meetingRoomNo = 1L;

        EntryResponse entryResponse = new EntryResponse(
                code,
                entryTime,
                meetingRoomNo
        );

        ResponseEntity<EntryResponse> mockResponse = ResponseEntity.ok(entryResponse);

        when(bookingAdaptor.checkBooking(eq(1L), any(EntryRequest.class))).thenReturn(mockResponse);

        EntryResponse actualResponse = meetingRoomService.enterMeetingRoom(1L, code, entryTime, meetingRoomNo);

        assertNotNull(actualResponse);
        assertEquals(code, actualResponse.getCode());
        assertEquals(entryTime, actualResponse.getEntryTime());
        assertEquals(meetingRoomNo, actualResponse.getMeetingRoomNo());

    }

    @Test
    @DisplayName("회의실 입실 실패")
    void enterMeetingRoomFailed() {
        String code = "ABC34F21";
        LocalDateTime entryTime = LocalDateTime.now();
        Long meetingRoomNo = 1L;

        EntryResponse entryResponse = new EntryResponse(
                code,
                entryTime,
                meetingRoomNo
        );

        ResponseEntity<EntryResponse> mockResponse = ResponseEntity.status(HttpStatus.BAD_REQUEST).body(entryResponse);

        when(bookingAdaptor.checkBooking(Mockito.eq(1L), Mockito.any(EntryRequest.class))).thenReturn(mockResponse);

        assertThrows(BookingNotFoundException.class, () -> meetingRoomService.enterMeetingRoom(1L, code, entryTime, meetingRoomNo));

    }
}