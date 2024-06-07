package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.seohauniv.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "staff")
@Getter
@Setter
public class Staff extends BaseTimeEntity {
    @Id
    @Column(name = "member_id")
    private String id;

    private String name;

    private String email;

    private LocalDate birth;

    private String phone;

    private String address;

    @MapsId
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;

    public Staff() {
    }

    public Staff(MemberFormDto memberFormDto) {
        this.name = memberFormDto.getName();
        this.email = memberFormDto.getEmail();
        this.birth = memberFormDto.getBirth();
        this.phone = memberFormDto.getPhone();
        this.address = memberFormDto.getAddress();
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
