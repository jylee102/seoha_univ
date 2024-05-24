package com.seohauniv.entity;

import jakarta.persistence.*;

public class WeeklyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;
}
