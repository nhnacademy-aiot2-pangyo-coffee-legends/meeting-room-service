package com.nhnacademy.meetingroomservice.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equipments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no", nullable = false)
    private Long no;


    @Enumerated(EnumType.STRING)
    @Column(name = "equipment_type", nullable = false, length = 50)
    private EquipmentType equipmentType;

    private String name;

    public Equipment(EquipmentType equipmentType, String name) {
        this.equipmentType = equipmentType;
        this.name = name;
    }
}
