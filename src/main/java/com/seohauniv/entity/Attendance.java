package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attendance")
@Data
@JsonIgnoreProperties({"student"})
public class Attendance extends BaseTimeEntity {
    @Id
    @Column(name = "attendance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;
    @Enumerated(EnumType.STRING)
    private Day day;

    @Enumerated(EnumType.STRING)
    @Column(name = "attend_status")
    private AttendStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enroll_id")
    private Enroll enroll;


}
