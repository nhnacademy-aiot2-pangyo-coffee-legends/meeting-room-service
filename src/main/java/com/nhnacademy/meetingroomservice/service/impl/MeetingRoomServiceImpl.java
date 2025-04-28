package com.nhnacademy.meetingroomservice.service.impl;

import com.nhnacademy.meetingroomservice.domain.MeetingRoom;
import com.nhnacademy.meetingroomservice.dto.MeetingRoomResponse;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomNotFoundException;
import com.nhnacademy.meetingroomservice.repository.MeetingRoomRepository;
import com.nhnacademy.meetingroomservice.service.MeetingRoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class MeetingRoomServiceImpl implements MeetingRoomService {

    private final MeetingRoomRepository meetingRoomRepository;

    @Override
    public MeetingRoomResponse getMeetingRoom(Long no) {
        MeetingRoom meetingRoom = meetingRoomRepository.findById(no).orElseThrow(() -> new MeetingRoomNotFoundException(no));

        return convertToMeetingRoomResponse(meetingRoom);
    }

    private MeetingRoomResponse convertToMeetingRoomResponse(MeetingRoom meetingRoom) {
        return new MeetingRoomResponse(
                meetingRoom.getNo(),
                meetingRoom.getMeetingRoomName(),
                meetingRoom.getMeetingRoomCapacity()
        );
    }
}
