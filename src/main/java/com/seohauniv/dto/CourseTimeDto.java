package com.seohauniv.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTimeDto {
    private String day;

    @NotNull(message = "선택된 요일의 강의 시작 시간을 입력하세요.")
    private String startTime;

    @NotNull(message = "선택된 요일의 강의 종료 시간을 입력하세요.")
    private String endTime;
}
