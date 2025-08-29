package com.nhnacademy.meetingroomservice.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.meetingroomservice.controller.MeetingRoomController;
import com.nhnacademy.meetingroomservice.error.CommonErrorResponse;
import com.nhnacademy.meetingroomservice.exception.BookingNotFoundException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomAlreadyExistsException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomDoesNotExistException;
import com.nhnacademy.meetingroomservice.exception.MeetingRoomNotFoundException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = MeetingRoomController.class)
public class MeetingRoomControllerAdvice {

    @ExceptionHandler(MeetingRoomAlreadyExistsException.class)
    public ResponseEntity<CommonErrorResponse> meetingRoomAlreadyExistsExceptionHandler(MeetingRoomAlreadyExistsException meetingRoomAlreadyExistsException, HttpServletRequest request) {

        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                meetingRoomAlreadyExistsException.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(commonErrorResponse);
    }

    @ExceptionHandler(MeetingRoomNotFoundException.class)
    public ResponseEntity<CommonErrorResponse> meetingRoomNotFoundExceptionHandler(MeetingRoomNotFoundException meetingRoomNotFoundException, HttpServletRequest request) {

        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                meetingRoomNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(commonErrorResponse);
    }

    @ExceptionHandler(MeetingRoomDoesNotExistException.class)
    public ResponseEntity<CommonErrorResponse> meetingRoomDoesNotExistExceptionHandler(MeetingRoomDoesNotExistException meetingRoomDoesNotExistException, HttpServletRequest request) {

        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                meetingRoomDoesNotExistException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(commonErrorResponse);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<CommonErrorResponse> bookingNotFoundException(BookingNotFoundException bookingNotFoundException, HttpServletRequest request) {
        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                bookingNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(commonErrorResponse);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<CommonErrorResponse> handleGenericException(Throwable e, HttpServletRequest request) {

        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(commonErrorResponse);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<CommonErrorResponse> handlerFeignException(FeignException e, HttpServletRequest request) throws JsonProcessingException {
        HttpStatus status = HttpStatus.resolve(e.status());

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message = jsonParser(e.contentUTF8());

        CommonErrorResponse commonErrorResponse = new CommonErrorResponse(
                message,
                status.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(commonErrorResponse);
    }

    private String jsonParser(String feignMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(feignMessage);

        return node.get("message").asText();
    }
}
