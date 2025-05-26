CREATE TABLE meeting_rooms (
    meeting_room_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    meeting_room_name VARCHAR(25) NOT NULL COMMENT '회의실이름',
    meeting_room_capacity INT NOT NULL COMMENT '수용인원'
) COMMENT = '회의실';