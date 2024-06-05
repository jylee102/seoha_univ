package com.seohauniv.entity;

import com.seohauniv.constant.Role;
import com.seohauniv.util.IdGenerator;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
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
}
