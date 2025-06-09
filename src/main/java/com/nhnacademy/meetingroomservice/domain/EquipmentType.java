package com.nhnacademy.meetingroomservice.domain;

public enum EquipmentType {

    DISPLAY(1L, "디스플레이"),

    DIGITAL_WHITEBOARD(2L, "전자 화이트보드"),

    VIDEO_CONFERENCE_SYSTEM(3L, "화상회의 시스템");

    final Long id;
    final String name;

    EquipmentType(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
