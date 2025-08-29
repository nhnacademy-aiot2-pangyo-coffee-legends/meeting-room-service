package com.nhnacademy.meetingroomservice.service.impl;

import com.nhnacademy.meetingroomservice.domain.Equipment;
import com.nhnacademy.meetingroomservice.dto.EquipmentResponse;
import com.nhnacademy.meetingroomservice.repository.EquipmentRepository;
import com.nhnacademy.meetingroomservice.service.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Override
    @Transactional(readOnly = true)
    public List<EquipmentResponse> getEquipments() {
        List<Equipment> equipments = equipmentRepository.findAll();

        if (!equipments.isEmpty()) {

            return equipments.stream()
                    .map(equipment -> new EquipmentResponse(equipment.getNo(), equipment.getName()))
                    .toList();

        } else {

            return new ArrayList<>();
        }
    }
}
