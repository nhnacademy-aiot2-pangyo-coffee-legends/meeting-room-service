package com.nhnacademy.meetingroomservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EntryRequest {

    private String code;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime entryTime;

    private Long bookingNo;

}
