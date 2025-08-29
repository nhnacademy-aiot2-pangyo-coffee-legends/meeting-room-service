package com.nhnacademy.meetingroomservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EquipmentResponse {

    private final Long equipmentNo;

    private final String equipmentName;
}
