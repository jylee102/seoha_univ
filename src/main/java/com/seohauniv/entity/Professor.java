package com.seohauniv.entity;

import com.seohauniv.util.IdGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "professor")
@Getter
@Setter
@ToString
public class Professor {
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
            this.id = IdGenerator.generateProfessorId(LocalDateTime.now());
        }
    }

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
