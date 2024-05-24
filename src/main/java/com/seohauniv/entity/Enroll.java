package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "enroll")
@Data
public class Enroll {
    @Id
    @Column(name = "enroll_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;
}
