package com.seohauniv.dto;

import com.seohauniv.constant.Day;
import com.seohauniv.entity.CourseTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
public class AttendanceWeekListDto {
    private int week;
    private CourseTime courseTime;
    private String courseName;


}
