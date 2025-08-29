package com.nhnacademy.meetingroomservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "meeting_room_equipment")
@Getter
@NoArgsConstructor
public class MeetingRoomEquipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meeting_room_equipment_no")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_room_no")
    private MeetingRoom meetingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_no")
    private Equipment equipment;

    public MeetingRoomEquipment(MeetingRoom meetingRoom, Equipment equipment) {
        this.meetingRoom = meetingRoom;
        this.equipment = equipment;
    }
}
