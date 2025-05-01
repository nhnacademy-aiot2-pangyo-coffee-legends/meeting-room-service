package com.nhnacademy.meetingroomservice.exception;

public class MeetingRoomAlreadyExistsException extends RuntimeException {
    public MeetingRoomAlreadyExistsException(String meetingRoomName) {
        super("%s은(는) 이미 존재하는 회의실입니다.".formatted(meetingRoomName));
    }
}
