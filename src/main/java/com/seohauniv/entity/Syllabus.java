package com.seohauniv.entity;

import com.seohauniv.constant.CourseType;
import com.seohauniv.constant.ProcedureStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "syllabus")
@Data
public class Syllabus {
    @Id
    @Column(name = "syllabus_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_type")
    @Enumerated(EnumType.STRING)
    private CourseType courseType;

    private int credit; // 이수학점

    private String overview; // 개요

    private String objective; // 강의 목표

    private String textbook; // 교재

    @Enumerated(EnumType.STRING)
    private ProcedureStatus status;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyPlan> weeklyPlans;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTime> courseTimes;
}
