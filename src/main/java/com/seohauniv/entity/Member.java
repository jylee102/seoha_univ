package com.seohauniv.entity;

import com.seohauniv.constant.Role;
import com.seohauniv.util.IdGenerator;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Data
//@Inheritance(strategy = InheritanceType.JOINED)
public class Member extends BaseTimeEntity {
    @Id
    @Column(name = "member_id", updatable = false, nullable = false)
    private String id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // 양방향
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Professor professor;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Staff staff;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            if (this.staff != null) this.id = IdGenerator.generateStaffId(this.getRegDate());
            else if (this.student != null) this.id = IdGenerator.generateStudentId(this.getRegDate());
            else if (this.professor != null) this.id = IdGenerator.generateProfessorId(this.getRegDate());
        }
    }
}
