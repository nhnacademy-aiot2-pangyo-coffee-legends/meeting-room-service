package com.nhnacademy.meetingroomservice.service;

import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;

public interface MeetingRoomService {

    MeetingRoomResponse createMeetingRoom(String meetingRoomName, int meetingRoomCapacity);

    MeetingRoomResponse getMeetingRoom(Long no);

    MeetingRoomResponse updateMeetingRoom(Long no, String meetingRoomName, int meetingRoomCapacity);

    void deleteMeetingRoom(Long no);
}
