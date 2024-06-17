package com.seohauniv.dto;

import com.seohauniv.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceStudentListDto {
    private String studentId;
    private String studentName;
    private String majorName;
    private String status;
    private Course course;
}
