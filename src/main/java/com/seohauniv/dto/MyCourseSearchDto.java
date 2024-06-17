package com.seohauniv.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class MyCourseSearchDto {
    private int searchYear; // 연도
    private int searchSemester;

}
