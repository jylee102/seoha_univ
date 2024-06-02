package com.seohauniv.dto;

import com.seohauniv.constant.Day;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Syllabus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class CourseTimeDto {
    private Day day;

    @NotNull(message = "선택된 요일의 강의 시작 시간을 입력하세요.")
    private LocalTime startTime;

    @NotNull(message = "선택된 요일의 강의 종료 시간을 입력하세요.")
    private LocalTime endTime;
    private static ModelMapper modelMapper = new ModelMapper();

    public CourseTimeDto() {
    }

    public CourseTimeDto(Day day, LocalTime startTime, LocalTime endTime) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getCourseTimeDescription() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return this.startTime.format(formatter) + " - " +
                this.endTime.format(formatter);
    }

    // dto -> entity
    public CourseTime toEntity() {
        return modelMapper.map(this, CourseTime.class);
    }

    // entity -> dto
    public static CourseTimeDto of(CourseTime courseTime) {
        return modelMapper.map(courseTime, CourseTimeDto.class);
    }
}
