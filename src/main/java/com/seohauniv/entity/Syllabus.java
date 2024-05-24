package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "syllabus")
@Data
public class Syllabus {
    @Id
    @Column(name = "syllabus_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String overview; // 개요

    private String objective; // 강의 목표

    private String textbook; // 교재
}
