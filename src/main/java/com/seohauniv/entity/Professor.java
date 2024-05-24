package com.seohauniv.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "professor")
@Getter
@Setter
@ToString
public class Professor extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private String id;

    private String name;

    private String email;

    private LocalDate birth;

    private String phone;

    private String address;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Dept major;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
}
