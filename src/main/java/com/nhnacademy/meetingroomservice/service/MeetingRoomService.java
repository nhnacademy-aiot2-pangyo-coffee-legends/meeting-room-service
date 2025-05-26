package com.nhnacademy.meetingroomservice.service;

import com.nhnacademy.meetingroomservice.dto.EntryResponse;
import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRoomService {

    MeetingRoomResponse registerMeetingRoom(String meetingRoomName, int meetingRoomCapacity);

    MeetingRoomResponse getMeetingRoom(Long no);

    List<MeetingRoomResponse> getMeetingRoomList();

    MeetingRoomResponse updateMeetingRoom(Long no, String meetingRoomName, int meetingRoomCapacity);

    void deleteMeetingRoom(Long no);

    EntryResponse enterMeetingRoom(String code, LocalDateTime entryTime, Long bookingNo);
}
