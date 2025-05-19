package com.nhnacademy.meetingroomservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.meetingroomservice.adaptor.BookingAdaptor;
import com.nhnacademy.meetingroomservice.dto.EntryRequest;
import com.nhnacademy.meetingroomservice.dto.EntryResponse;
import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;
import com.nhnacademy.meetingroomservice.exception.BookingNotFoundException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomAlreadyExistsException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomDoesNotExistException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomNotFoundException;
import com.nhnacademy.meetingroomservice.service.MeetingRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingRoomController.class)
class MeetingRoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MeetingRoomService meetingRoomService;

    @MockitoBean
    private BookingAdaptor bookingAdaptor;

    @Test
    @DisplayName("회의실 등록 성공")
    void registerMeetingRoom() throws Exception {
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;
        Long id = 1L;

        MeetingRoomResponse response = new MeetingRoomResponse(id, meetingRoomName, meetingRoomCapacity);

        when(meetingRoomService.registerMeetingRoom(meetingRoomName, meetingRoomCapacity)).thenReturn(response);

        mockMvc.perform(post("/api/v1/meeting-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "meetingRoomName": "회의실 test",
                                    "meetingRoomCapacity": 10
                                }
                                """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.meetingRoomName").value("회의실 test"))
                .andExpect(jsonPath("$.meetingRoomCapacity").value(10))
                .andDo(print());
    }

    @Test
    @DisplayName("회의실 중복 등록 실패 - 409 Conflict")
    void registerAlreadyExistingMeetingRoom() throws Exception {

        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;

        when(meetingRoomService.registerMeetingRoom(meetingRoomName, meetingRoomCapacity))
                .thenThrow(new MeetingRoomAlreadyExistsException(meetingRoomName));

        mockMvc.perform(post("/api/v1/meeting-rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                            {
                                            "meetingRoomName": "회의실 test",
                                            "meetingRoomCapacity": 10
                                            }
                                        """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value(String.format("%s은(는) 이미 존재하는 회의실입니다.", meetingRoomName)))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.uri").value("/api/v1/meeting-rooms"))
                .andDo(print());
    }

    @Test
    @DisplayName("회의실 조회 성공")
    void getMeetingRoom() throws Exception {
        String meetingRoomName = "회의실 test";
        int meetingRoomCapacity = 10;
        Long id = 1L;

        MeetingRoomResponse response = new MeetingRoomResponse(id, meetingRoomName, meetingRoomCapacity);

        when(meetingRoomService.getMeetingRoom(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/meeting-rooms/{meeting-room-id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.no").value(id))
                .andExpect(jsonPath("$.meetingRoomName").value(meetingRoomName))
                .andExpect(jsonPath("$.meetingRoomCapacity").value(meetingRoomCapacity))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 회의실 조회 - 404 Not Found")
    void getNonExistentMeetingRoom() throws Exception {
        Long id = 1L;

        when(meetingRoomService.getMeetingRoom(id))
                .thenThrow(new MeetingRoomNotFoundException(id));

        mockMvc.perform(get("/api/v1/meeting-rooms/{meeting-room-id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("회의실 %s을(를) 찾지 못했습니다.", id)))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.uri").value(String.format("/api/v1/meeting-rooms/%d", id)))
                .andDo(print());
    }

    @Test
    @DisplayName("회의실 목록 조회 성공")
    void getMeetingRoomList() throws Exception {

        MeetingRoomResponse response = new MeetingRoomResponse(1L, "회의실 test1", 10);
        MeetingRoomResponse response2 = new MeetingRoomResponse(2L, "회의실 test2", 15);

        List<MeetingRoomResponse> meetingRoomResponseList = List.of(response, response2);

        when(meetingRoomService.getMeetingRoomList()).thenReturn(meetingRoomResponseList);

        mockMvc.perform(get("/api/v1/meeting-rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].no").value(response.getNo()))
                .andExpect(jsonPath("$[0].meetingRoomName").value(response.getMeetingRoomName()))
                .andExpect(jsonPath("$[0].meetingRoomCapacity").value(response.getMeetingRoomCapacity()))
                .andExpect(jsonPath("$[1].no").value(response2.getNo()))
                .andExpect(jsonPath("$[1].meetingRoomName").value(response2.getMeetingRoomName()))
                .andExpect(jsonPath("$[1].meetingRoomCapacity").value(response2.getMeetingRoomCapacity()))
                .andDo(print());

    }

    @Test
    @DisplayName("회의실 정보 업데이트 성공")
    void updateMeetingRoom() throws Exception {
        Long id = 1L;
        MeetingRoomResponse updated = new MeetingRoomResponse(id, "회의실 updated", 15);

        when(meetingRoomService.updateMeetingRoom(id, "회의실 updated", 15)).thenReturn(updated);

        mockMvc.perform(put("/api/v1/meeting-rooms/{meeting-room-id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                             {
                                             "meetingRoomName": "회의실 updated",
                                             "meetingRoomCapacity": 15
                                             }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meetingRoomName").value("회의실 updated"))
                .andExpect(jsonPath("$.meetingRoomCapacity").value(15))
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 회의실 업데이트 시도 - 404 Not Found")
    void updateNonExistentMeetingRoom() throws Exception {
        String meetingRoomName = "회의실 updated";
        int meetingRoomCapacity = 15;
        Long nonExistentId = 1L;

        when(meetingRoomService.updateMeetingRoom(nonExistentId, meetingRoomName, meetingRoomCapacity))
                .thenThrow(new MeetingRoomDoesNotExistException(nonExistentId));

        mockMvc.perform(put("/api/v1/meeting-rooms/{meeting-room-id}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                            {
                                            "meetingRoomName": "회의실 updated",
                                            "meetingRoomCapacity": 15
                                            }
                                        """
                        ))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("회의실 %d은(는) 등록되지 않은 회의실입니다.", nonExistentId)))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.uri").value(String.format("/api/v1/meeting-rooms/%d", nonExistentId)))
                .andDo(print());
    }

    @Test
    @DisplayName("회의실 삭제 성공")
    void deleteMeetingRoom() throws Exception {

        Long nonExistentId = 1L;

        mockMvc.perform(delete("/api/v1/meeting-rooms/{meeting-room-id}", nonExistentId))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 회의실 삭제 시도 - 404 Not Found")
    void deleteNonExistentMeetingRoom() throws Exception {

        Long nonExistentId = 1L;

        doThrow(new MeetingRoomDoesNotExistException(nonExistentId))
                .when(meetingRoomService).deleteMeetingRoom(nonExistentId);

        mockMvc.perform(delete("/api/v1/meeting-rooms/{meeting-room-id}", nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(String.format("회의실 %d은(는) 등록되지 않은 회의실입니다.", nonExistentId)))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.uri").value(String.format("/api/v1/meeting-rooms/%d", nonExistentId)))
                .andDo(print());
    }

    @Test
    @DisplayName("회의실 입실")
    void enterMeetingRoom() throws Exception {
        String code = "ABC34F21";
        LocalDateTime entryTime = LocalDateTime.now();
        Long meetingRoomNo = 1L;

        EntryResponse entryResponse = new EntryResponse(
                code,
                entryTime,
                meetingRoomNo
        );

        when(meetingRoomService.enterMeetingRoom(Mockito.anyString(), Mockito.anyString(), Mockito.any(LocalDateTime.class), Mockito.anyLong())).thenReturn(entryResponse);

        EntryRequest entryRequest = new EntryRequest(
                code,
                entryTime,
                meetingRoomNo
        );

        String json = objectMapper.writeValueAsString(entryRequest);

        mockMvc.perform(post("/api/v1/meeting-rooms/verify")
                        .header("X-USER", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(entryResponse.getCode()))
                .andExpect(jsonPath("$.entryTime").value(entryResponse.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .andExpect(jsonPath("$.bookingNo").value(entryResponse.getBookingNo()))
                .andDo(print());
    }

    @Test
    @DisplayName("회의실 입실 실패")
    void enterMeetingRoomFailed() throws Exception {
        Long no = 1L;
        String code = "ABC34F21";
        LocalDateTime entryTime = LocalDateTime.now();
        Long meetingRoomNo = 1L;

        EntryRequest entryRequest = new EntryRequest(
                code,
                entryTime,
                meetingRoomNo
        );

        String json = objectMapper.writeValueAsString(entryRequest);

        doThrow(new BookingNotFoundException())
                .when(meetingRoomService).enterMeetingRoom(Mockito.anyString(), Mockito.anyString(), Mockito.any(LocalDateTime.class), Mockito.anyLong());

        mockMvc.perform(post("/api/v1/meeting-rooms/verify")
                        .header("X-USER", "test@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("등록되지 않은 예약정보입니다."))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.uri").value("/api/v1/meeting-rooms/verify"))
                .andDo(print());
    }
}