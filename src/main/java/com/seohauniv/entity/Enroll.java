package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enroll")
@Getter
@Setter
@ToString
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
    @OneToMany(mappedBy = "enroll",cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Attendance> attendances = new ArrayList<>();
}
