package com.nhnacademy.meetingroomservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "meeting_rooms")
@Getter
@ToString
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

    @ManyToMany
    @JoinTable(
            name = "meeting_room_equipment",
            joinColumns = @JoinColumn(name = "meeting_room_id"),
            inverseJoinColumns = @JoinColumn(name = "equipment_id")
    )
    private List<Equipment> equipments = new ArrayList<>();

    private MeetingRoom(String meetingRoomName, int meetingRoomCapacity) {
        this.meetingRoomName = meetingRoomName;
        this.meetingRoomCapacity = meetingRoomCapacity;
    }

    public static MeetingRoom ofNewMeetingRoom(String meetingRoomName, int meetingRoomCapacity, List<Equipment> equipments) {
        MeetingRoom meetingRoom = new MeetingRoom(meetingRoomName, meetingRoomCapacity);
        meetingRoom.equipments.addAll(equipments);
        return meetingRoom;
    }

    public void update(String meetingRoomName, int meetingRoomCapacity, List<Equipment> equipments) {
        this.meetingRoomName = meetingRoomName;
        this.meetingRoomCapacity = meetingRoomCapacity;
        this.equipments = equipments;
    }
}
