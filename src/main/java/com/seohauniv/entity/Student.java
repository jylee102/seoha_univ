package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seohauniv.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "student")
@Getter
@Setter
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
    @JsonIgnore
    private Member member;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enroll> enrolls;

    public Student() {
    }

    public Student(MemberFormDto memberFormDto) {
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.birth = memberFormDto.getBirth();
        this.phone = memberFormDto.getPhone();
        this.address = memberFormDto.getAddress();
        this.major = memberFormDto.getDept();
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", grade=" + grade +
                ", semester=" + semester +
                ", graduationDate=" + graduationDate +
                ", major=" + major +
                '}';
    }
}
