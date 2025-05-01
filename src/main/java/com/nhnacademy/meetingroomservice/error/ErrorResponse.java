package com.nhnacademy.meetingroomservice.error;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final String message;
    private final int statusCode;
    private final String uri;

    public ErrorResponse(String message, int statusCode, String uri) {
        this.message = message;
        this.statusCode = statusCode;
        this.uri = uri;
    }
}
