package com.nhnacademy.meetingroomservice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

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

    private MeetingRoom(String meetingRoomName, int meetingRoomCapacity) {
        this.meetingRoomName = meetingRoomName;
        this.meetingRoomCapacity = meetingRoomCapacity;
    }

    public static MeetingRoom ofNewMeetingRoom(String meetingRoomName, int meetingRoomCapacity) {
        return new MeetingRoom(meetingRoomName, meetingRoomCapacity);
    }

    public void update(String meetingRoomName, int meetingRoomCapacity) {
        this.meetingRoomName = meetingRoomName;
        this.meetingRoomCapacity = meetingRoomCapacity;
    }
}
