package com.seohauniv.entity;

import com.seohauniv.constant.AcademicStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "academic_stat")
@Data
public class AcademicStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AcademicStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Student student;
}
