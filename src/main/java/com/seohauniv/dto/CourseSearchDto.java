package com.seohauniv.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seohauniv.constant.CourseType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSearchDto {
    private String searchQuery = "";
    private CourseType searchCourseType;
}
