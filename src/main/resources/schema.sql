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

CREATE TABLE `meeting_room_equipment` (
                                          `meeting_room_equipment_no` BIGINT AUTO_INCREMENT PRIMARY KEY,
                                          `meeting_room_no`	BIGINT	NOT NULL,
                                          `equipment_no`	BIGINT	NOT NULL
);


ALTER TABLE `meeting_room_equipment`
    ADD CONSTRAINT `FK_MEETING_ROOM` FOREIGN KEY (`meeting_room_no`) REFERENCES meeting_rooms(`meeting_room_no`),
    ADD CONSTRAINT `FK_EQUIPMENT` FOREIGN KEY (`equipment_no`) REFERENCES equipments(`equipment_no`);