package com.nhnacademy.meetingroomservice.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.meetingroomservice.controller.MeetingRoomController;
import com.nhnacademy.meetingroomservice.error.ErrorResponse;
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
    public ResponseEntity<ErrorResponse> meetingRoomAlreadyExistsExceptionHandler(MeetingRoomAlreadyExistsException meetingRoomAlreadyExistsException, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                meetingRoomAlreadyExistsException.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    @ExceptionHandler(MeetingRoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> meetingRoomNotFoundExceptionHandler(MeetingRoomNotFoundException meetingRoomNotFoundException, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                meetingRoomNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(MeetingRoomDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> meetingRoomDoesNotExistExceptionHandler(MeetingRoomDoesNotExistException meetingRoomDoesNotExistException, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                meetingRoomDoesNotExistException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> bookingNotFoundException(BookingNotFoundException bookingNotFoundException, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                bookingNotFoundException.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Throwable e, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handlerFeignException(FeignException e, HttpServletRequest request) throws JsonProcessingException {
        HttpStatus status = HttpStatus.resolve(e.status());

        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        String message = jsonParser(e.contentUTF8());

        ErrorResponse errorResponse = new ErrorResponse(
                "예약 서비스 응답 오류: " + message,
                status.value(),
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }

    private String jsonParser(String feignMessage) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(feignMessage);

        return node.get("message").asText();
    }
}
