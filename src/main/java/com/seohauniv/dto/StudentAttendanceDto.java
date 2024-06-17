package com.seohauniv.dto;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Evaluation;
import com.seohauniv.entity.Student;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentAttendanceDto {
    private Student student;
    private Evaluation evaluation;
    private int countPresent;
    private int countLate;
    private int countAbsent;
    private Course course;
}
