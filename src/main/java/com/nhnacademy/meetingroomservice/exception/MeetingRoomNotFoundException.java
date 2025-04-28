package com.nhnacademy.meetingroomservice.exception;

public class MeetingRoomNotFoundException extends RuntimeException {

    public MeetingRoomNotFoundException(Long no) {
        super("회의실 %s를 찾지 못했습니다.".formatted(no));
    }
}
