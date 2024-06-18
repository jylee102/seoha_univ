package com.seohauniv.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TotalGradeDto {
    private int year;
    private int semester;
    private int sumGrades;
    private Float average;
}
