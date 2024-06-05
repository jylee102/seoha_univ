package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "room")
@Data
public class Room {
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "room_number")
    private int roomNo;
}
