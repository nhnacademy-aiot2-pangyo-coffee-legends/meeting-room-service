package com.nhnacademy.meetingroomservice.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookingResponse {

    private Long no;

    private String code;

    @EqualsAndHashCode.Exclude
    private LocalDateTime date;

    private Integer attendeeCount;

    @EqualsAndHashCode.Exclude
    private LocalDateTime finishedAt;

    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    private Long mbNo;

    private String mbName;

    private String changeName;

    private Long roomNo;

    private String roomName;
}
