package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
public class Student extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private String id;

    private String name;

    private String email;

    private LocalDate birth;

    private String phone;

    private String address;

    private int grade;

    private int semester;

    @Column(name = "graduation_date")
    private LocalDate graduationDate;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept major;

    @MapsId // 식별 관계 표현
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
