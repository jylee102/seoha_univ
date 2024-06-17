package com.seohauniv.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@JsonIgnoreProperties({"student"})
public class Attendance extends BaseTimeEntity {
    @Id
    @Column(name = "attendance_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int week;
    @Enumerated(EnumType.STRING)
    private Day day;

    @Enumerated(EnumType.STRING)
    @Column(name = "attend_status")
    private AttendStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enroll_id")
    @JsonIgnore
    private Enroll enroll;

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", week=" + week +
                ", day=" + day +
                ", status=" + status +
                '}';
    }
}
