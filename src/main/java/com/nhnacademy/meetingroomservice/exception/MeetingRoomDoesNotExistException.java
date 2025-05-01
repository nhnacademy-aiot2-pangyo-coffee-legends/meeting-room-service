package com.nhnacademy.meetingroomservice.exception;

public class MeetingRoomDoesNotExistException extends RuntimeException {
    public MeetingRoomDoesNotExistException(String meetingRoomName) {
        super("회의실 %s은(는) 등록되지 않은 회의실입니다.".formatted(meetingRoomName));
    }

    public MeetingRoomDoesNotExistException(Long no) {
        super("회의실 %d은(는) 등록되지 않은 회의실입니다.".formatted(no));
    }
}
