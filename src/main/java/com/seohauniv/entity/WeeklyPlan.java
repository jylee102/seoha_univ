package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "weekly_plan")
@Data
public class WeeklyPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "weekly_plan_id")
    private Long id;

    private int week;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;

    public WeeklyPlan() {
    }

    public WeeklyPlan(int week, String content, Syllabus syllabus) {
        this.week = week;
        this.content = content;
        this.syllabus = syllabus;
    }
}
