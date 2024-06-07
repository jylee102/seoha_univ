package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seohauniv.constant.Day;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "course_time")
@Getter
@Setter
public class CourseTime {
    @Id
    @Column(name = "course_time_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    private LocalTime startTime;
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id")
    @JsonIgnore
    private Syllabus syllabus;

    @Override
    public String toString() {
        return "CourseTime{" +
                "id=" + id +
                ", day=" + day +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
