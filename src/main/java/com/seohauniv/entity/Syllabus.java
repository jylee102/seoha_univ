package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seohauniv.constant.CourseType;
import com.seohauniv.constant.ProcedureStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "syllabus")
@Getter
@Setter
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
    
    private int year; // 연도
    private int semester; // 학기

    private int capacity; // 수강정원
    

    @Enumerated(EnumType.STRING)
    private ProcedureStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Professor professor;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WeeklyPlan> weeklyPlans;

    @OneToMany(mappedBy = "syllabus", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CourseTime> courseTimes;

    // CourseType의 설명을 얻는 메소드
    public String getCourseTypeDescription() {
        return this.courseType.getDescription();
    }

    @Override
    public String toString() {
        return "Syllabus{" +
                "id=" + id +
                ", courseName='" + courseName + '\'' +
                ", courseType=" + courseType +
                ", credit=" + credit +
                ", overview='" + overview + '\'' +
                ", objective='" + objective + '\'' +
                ", textbook='" + textbook + '\'' +
                ", status=" + status +
                ", weeklyPlans=" + weeklyPlans +
                ", courseTimes=" + courseTimes +
                '}';
    }
}
