package com.nhnacademy.meetingroomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@AllArgsConstructor
public class MeetingRoomRegisterRequest {

    private String meetingRoomName;

    private int meetingRoomCapacity;

    private List<Long> equipmentIds;

}
