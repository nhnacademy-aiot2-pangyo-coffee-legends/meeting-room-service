package com.nhnacademy.meetingroomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EntryResponse {

    private String code;

    private LocalDateTime entryTime;

    private Long meetingRoomNo;

}
