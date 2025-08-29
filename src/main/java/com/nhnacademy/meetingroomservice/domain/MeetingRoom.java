package com.nhnacademy.meetingroomservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Table(name = "meeting_rooms")
@Getter
@ToString(exclude = "meetingRoomEquipments")
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meeting_room_no")
    @Comment("회의실ID")
    Long no;

    @Column(name="meeting_room_name", length=10)
    @Comment("회의실이름")
    String meetingRoomName;

    @Column(name="meeting_room_capacity")
    @Comment("수용인원")
    int meetingRoomCapacity;

    @OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MeetingRoomEquipment> meetingRoomEquipments = new ArrayList<>();

    private MeetingRoom(String meetingRoomName, int meetingRoomCapacity) {
        this.meetingRoomName = meetingRoomName;
        this.meetingRoomCapacity = meetingRoomCapacity;
    }

    public static MeetingRoom ofNewMeetingRoom(String meetingRoomName, int meetingRoomCapacity, List<Equipment> equipments) {
        MeetingRoom meetingRoom = new MeetingRoom(meetingRoomName, meetingRoomCapacity);
        meetingRoom.meetingRoomEquipments = meetingRoomEquipmentMapping(meetingRoom, equipments);
        return meetingRoom;
    }

    public void update(String meetingRoomName, int meetingRoomCapacity, List<Equipment> equipments) {
        this.meetingRoomName = meetingRoomName;
        this.meetingRoomCapacity = meetingRoomCapacity;

        this.meetingRoomEquipments.clear();

        this.meetingRoomEquipments.addAll(meetingRoomEquipmentMapping(this, equipments));
    }

    /**
     *
     * @param meetingRoom 회의실-기자재 entity mapping에 해당하는 회의실
     * @param equipments 회의실-기자재 entity mapping에 해당하는 기자재
     * @return
     */
    private static List<MeetingRoomEquipment> meetingRoomEquipmentMapping(MeetingRoom meetingRoom, List<Equipment> equipments) {
        return equipments.stream().map(equipment -> new MeetingRoomEquipment(meetingRoom, equipment)).toList();
    }

    /**
     * 기존에 등록되어 있는 기자재의 목록을 확인하여 기자재가 중복등록 되지 않도록 검증하는 method
     *
     * @param newEquipments 추가하는 기자재 entity list
     */
    private void addEquipments(List<Equipment> newEquipments) {
        Set<EquipmentType> existingTypes = meetingRoomEquipments.stream()
                .map(meetingRoomEquipment -> meetingRoomEquipment.getEquipment().getEquipmentType())
                .collect(Collectors.toSet());

        newEquipments.stream()
                .filter(newEquipment -> !existingTypes.contains(newEquipment.getEquipmentType()))
                .map(newEquipment -> new MeetingRoomEquipment(this, newEquipment))
                .forEach(meetingRoomEquipments::add);
    }
}
