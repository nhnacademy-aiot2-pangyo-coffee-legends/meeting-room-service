package com.nhnacademy.meetingroomservice.error;

import lombok.Getter;

@Getter
public class CommonErrorResponse {

    private final String message;
    private final int statusCode;
    private final String uri;

    public CommonErrorResponse(String message, int statusCode, String uri) {
        this.message = message;
        this.statusCode = statusCode;
        this.uri = uri;
    }
}
