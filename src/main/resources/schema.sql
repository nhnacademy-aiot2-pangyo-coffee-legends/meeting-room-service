CREATE TABLE meeting_rooms (
                               meeting_room_no BIGINT AUTO_INCREMENT PRIMARY KEY,
                               meeting_room_name VARCHAR(25) NOT NULL,
                               meeting_room_capacity INT NOT NULL
);

CREATE TABLE equipments (
    equipment_no BIGINT AUTO_INCREMENT PRIMARY KEY,
    equipment_type VARCHAR(50) NOT NULL,
    equipment_name VARCHAR(50) NOT NULL
);
