package com.nhnacademy.meetingroomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class MeetingRoomRegisterRequest {

    private String meetingRoomName;

    private int meetingRoomCapacity;

}
