package com.nhnacademy.meetingroomservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.hibernate.annotations.Comment;

@Entity
@NoArgsConstructor
@Table(name = "meeting_rooms")
@Getter
@ToString
public class MeetingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회의실ID")
    Long no;

    @Column(name="meeting_room_name", length=10)
    @Comment("회의실이름")
    String meetingRoomName;

    @Column(name="meeting_room_capacity")
    @Comment("수용인원")
    int meetingRoomCapacity;
}
