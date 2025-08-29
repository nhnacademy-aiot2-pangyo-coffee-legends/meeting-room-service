package com.nhnacademy.meetingroomservice.repository;

import com.nhnacademy.meetingroomservice.domain.MeetingRoomEquipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomEquipmentRepository extends JpaRepository<MeetingRoomEquipment, Long> {
}