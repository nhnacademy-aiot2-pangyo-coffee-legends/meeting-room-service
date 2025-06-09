package com.nhnacademy.meetingroomservice.repository;

import com.nhnacademy.meetingroomservice.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepository extends JpaRepository<Equipment, Long> {
}
