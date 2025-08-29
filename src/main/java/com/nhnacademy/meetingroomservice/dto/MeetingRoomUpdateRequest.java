package com.nhnacademy.meetingroomservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MeetingRoomUpdateRequest {

    private String meetingRoomName;

    private int meetingRoomCapacity;

    private List<Long> equipmentIds;
}
