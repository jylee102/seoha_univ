package com.seohauniv.dto;

import com.seohauniv.constant.Day;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Syllabus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.time.LocalTime;

@Getter
@Setter
public class CourseTimeDto {
    private Day day;

    @NotNull(message = "선택된 요일의 강의 시작 시간을 입력하세요.")
    private LocalTime startTime;

    @NotNull(message = "선택된 요일의 강의 종료 시간을 입력하세요.")
    private LocalTime endTime;
    private static ModelMapper modelMapper = new ModelMapper();

    // dto -> entity
    public CourseTime toEntity() {
        return modelMapper.map(this, CourseTime.class);
    }

    // entity -> dto
    public static CourseTimeDto of(CourseTime courseTime) {
        return modelMapper.map(courseTime, CourseTimeDto.class);
    }
}
