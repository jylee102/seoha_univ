package com.seohauniv.entity;

import com.seohauniv.constant.LeaveReason;
import com.seohauniv.constant.ProcedureStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "break")
@Data
public class Break extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_grade")
    private int studentGrade;

    @Column(name = "from_year")
    private int fromYear;
    @Column(name = "from_semester")
    private int fromSemester;
    @Column(name = "to_year")
    private int toYear;
    @Column(name = "to_semester")
    private int toSemester;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason_type")
    private LeaveReason reasonType;

    @Enumerated(EnumType.STRING)
    private ProcedureStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Student student;
}
