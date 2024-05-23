package com.seohauniv.entity;

import com.seohauniv.util.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student")
@Getter
@Setter
@ToString
public class Student {
    @Id
    @Column(name = "member_id")
    private String id;

    private String name;

    private String email;

    private LocalDate birth;

    private String phone;

    private String address;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Dept major;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = IdGenerator.generateStudentId(LocalDateTime.now());
        }
    }

    // 식별 관계 표현
    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
