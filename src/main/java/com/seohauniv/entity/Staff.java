package com.seohauniv.entity;

import com.seohauniv.util.IdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff")
@Getter
@Setter
@ToString
public class Staff {
    @Id
    @Column(name = "member_id")
    private String id;

    private String name;

    private String email;

    private LocalDate birth;

    private String phone;

    private String address;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = IdGenerator.generateStaffId(LocalDateTime.now());
        }
    }

    @MapsId
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
