package com.nhnacademy.meetingroomservice.exception;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException() {
        super("등록되지 않은 예약정보입니다.");
    }
}
