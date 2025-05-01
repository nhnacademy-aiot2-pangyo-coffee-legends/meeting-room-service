package com.nhnacademy.meetingroomservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeetingRoomUpdateRequest {

    private String meetingRoomName;

    private int meetingRoomCapacity;
}
