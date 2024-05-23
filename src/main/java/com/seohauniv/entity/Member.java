package com.seohauniv.entity;

import com.seohauniv.constant.Role;
import jakarta.persistence.*;
import lombok.Data;

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
            if (this.staff != null) this.id = this.staff.getId();
            else if (this.student != null) this.id = this.student.getId();
            else if (this.professor != null) this.id = this.professor.getId();
        }
    }
}
