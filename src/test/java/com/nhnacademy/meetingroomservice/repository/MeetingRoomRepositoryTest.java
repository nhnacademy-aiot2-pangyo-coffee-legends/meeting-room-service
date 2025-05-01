package com.nhnacademy.meetingroomservice.repository;

import com.nhnacademy.meetingroomservice.domain.MeetingRoom;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
class MeetingRoomRepositoryTest {

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    @DisplayName("회의실 등록 test")
    void testRegisterMeetingRoom() {
        // Given
        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(
                "회의실 1",
                20
        );

        // When
        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        // Then
        assertNotNull(savedMeetingRoom);
        assertAll(
                () -> assertEquals("회의실 1", savedMeetingRoom.getMeetingRoomName()),
                () -> assertEquals(20, savedMeetingRoom.getMeetingRoomCapacity())
        );
    }

    @Test
    @DisplayName("회의실 조회 test")
    void testGetMeetingRoom() {

        // Given
        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(
                "회의실 B",
                25
        );

        // When
        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        entityManager.persist(savedMeetingRoom);

        entityManager.clear();

        // Then
        assertNotNull(savedMeetingRoom);
        boolean actual = meetingRoomRepository.existsById(savedMeetingRoom.getNo());
        assertTrue(actual);
    }

    @Test
    @DisplayName("회의실 정보 변경 test")
    void testUpdateMeetingRoom() {

        // Given
        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(
                "회의실 C",
                10
        );

        // When
        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        entityManager.persist(savedMeetingRoom);

        entityManager.clear();

        Optional<MeetingRoom> optionalMeetingRoom1 = meetingRoomRepository.findById(savedMeetingRoom.getNo());

        MeetingRoom dbMeetingRoom = optionalMeetingRoom1.get();

        dbMeetingRoom.update(
                "회의실 D",
                25
        );

        entityManager.flush();

        entityManager.clear();

        MeetingRoom updatedMeetingRoom = meetingRoomRepository.findById(dbMeetingRoom.getNo()).orElseThrow(() -> new RuntimeException("No updated meeting rooms."));

        // Then
        assertEquals("회의실 D", updatedMeetingRoom.getMeetingRoomName());
        assertEquals(25, updatedMeetingRoom.getMeetingRoomCapacity());

    }

    @Test
    @DisplayName("회의실 정보 삭제 test")
    void testDeleteMeetingRoom() {

        // Given
        log.info("Meeting room created.");
        MeetingRoom meetingRoom = MeetingRoom.ofNewMeetingRoom(
                "회의실 E",
                15
        );

        // When

        log.info("Meeting room saved.");
        MeetingRoom savedMeetingRoom = meetingRoomRepository.save(meetingRoom);

        log.info("Meeting room persisted.");
        entityManager.persist(savedMeetingRoom);

        log.info("Persistence context cleared.");
        entityManager.clear();

        log.info("Meeting room deleted.");
        meetingRoomRepository.deleteById(savedMeetingRoom.getNo());

        // Then
        log.info("Checks if meeting room exists.");
        boolean actual = meetingRoomRepository.existsById(savedMeetingRoom.getNo());
        assertFalse(actual);

    }

    @Test
    @DisplayName("존재하지 않는 회의실 조회 test")
    void testNonExistentMeetingRoom() {
        // When
        Optional<MeetingRoom> meetingRoom = meetingRoomRepository.findById(9999L);

        // Then
        assertTrue(meetingRoom.isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 회의실 삭제 test")
    void testDeleteNonExistentMeetingRoom() {
        // Given
        Long nonExistentId = 9999L;

        assertDoesNotThrow(() -> meetingRoomRepository.deleteById(nonExistentId));
    }
}