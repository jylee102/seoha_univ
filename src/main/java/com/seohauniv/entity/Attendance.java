package com.seohauniv.entity;

import com.seohauniv.constant.AttendStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "attendance")
@Data
public class Attendance extends BaseTimeEntity {
    @Id
    @Column(name = "attendance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "attend_status")
    private AttendStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id")
    private Evaluation evaluation;
}
