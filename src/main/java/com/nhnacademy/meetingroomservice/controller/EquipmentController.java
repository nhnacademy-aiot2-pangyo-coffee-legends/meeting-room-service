package com.nhnacademy.meetingroomservice.controller;

import com.nhnacademy.meetingroomservice.dto.EquipmentResponse;
import com.nhnacademy.meetingroomservice.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/meeting-rooms/equipments")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentResponse>> getEquipmentIds() {
        List<EquipmentResponse> equipmentResponse = equipmentService.getEquipments();

        return ResponseEntity.ok(equipmentResponse);
    }
}
