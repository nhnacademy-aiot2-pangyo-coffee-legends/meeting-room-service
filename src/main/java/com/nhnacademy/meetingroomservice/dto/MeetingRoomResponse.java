package com.nhnacademy.meetingroomservice.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class MeetingRoomResponse {

    private final Long no;

    private final String meetingRoomName;

    private final int meetingRoomCapacity;

    private final List<String> equipmentNames;
}
