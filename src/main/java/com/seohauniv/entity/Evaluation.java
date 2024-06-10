package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "evaluation")
@Data
public class Evaluation {
    @Id
    @Column(name = "evaluation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int homework;
    private int midExam;
    private int finalExam;

    @Column(name = "converted_score")
    private float convertedScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enroll_id")
    private Enroll enroll;
}
