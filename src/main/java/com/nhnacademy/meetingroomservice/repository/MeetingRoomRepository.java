package com.nhnacademy.meetingroomservice.repository;

import com.nhnacademy.meetingroomservice.domain.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

    boolean existsMeetingRoomByMeetingRoomName(String meetingRoomName);

}
