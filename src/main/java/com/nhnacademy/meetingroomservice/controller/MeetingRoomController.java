package com.nhnacademy.meetingroomservice.controller;

import com.nhnacademy.meetingroomservice.domain.MeetingRoom;
import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;
import com.nhnacademy.meetingroomservice.service.MeetingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meeting-rooms")
@RequiredArgsConstructor
public class MeetingRoomController {

    private final MeetingRoomService meetingRoomService;

    @GetMapping("/{meeting-room-id}")
    public ResponseEntity<MeetingRoomResponse> getMeetingRoom(@PathVariable Long no) {
        MeetingRoomResponse meetingRoomResponse = meetingRoomService.getMeetingRoom(no);

        return ResponseEntity
                .ok(meetingRoomResponse);
    }

}
