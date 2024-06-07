package com.seohauniv.entity;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.dto.ScheduleFormDto;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "schedule")
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_day")
    private LocalDate start;

    @Column(name = "end_day")
    private LocalDate end;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Staff staff;

    public void updateSchedule(ScheduleFormDto scheduleFormDto){
        this.start = scheduleFormDto.getStart();
        this.end = scheduleFormDto.getEnd();
        this.content = scheduleFormDto.getContent();
    }
}
