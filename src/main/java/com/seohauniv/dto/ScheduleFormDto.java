package com.seohauniv.dto;

import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Schedule;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data
public class ScheduleFormDto {
    private String memberId;

    private Long id;

    private LocalDate start;

    private LocalDate end;

    @NotNull(message = "내용을 입력해주세요.")
    private String content;

    private static ModelMapper modelMapper = new ModelMapper();

    public Schedule creatSchedule() {
        return modelMapper.map(this, Schedule.class);
    }

    public static ScheduleFormDto of(Schedule schedule) {
        return modelMapper.map(schedule, ScheduleFormDto.class);
    }


}
