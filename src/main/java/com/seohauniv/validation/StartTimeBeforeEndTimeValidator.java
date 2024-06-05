package com.seohauniv.validation;

import com.seohauniv.dto.CourseTimeDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartTimeBeforeEndTimeValidator implements ConstraintValidator<StartTimeBeforeEndTime, CourseTimeDto> {

    @Override
    public void initialize(StartTimeBeforeEndTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(CourseTimeDto courseTimeDto, ConstraintValidatorContext context) {
        if (courseTimeDto == null) {
            return true; // null 값은 유효한 것으로 처리
        }
        // 시작 시간과 종료 시간이 모두 null이 아닌 경우에만 유효성을 검사
        if (courseTimeDto.getStartTime() != null && courseTimeDto.getEndTime() != null) {
            // 시작 시간이 종료 시간보다 앞선 경우에만 유효
            return courseTimeDto.getStartTime().isBefore(courseTimeDto.getEndTime());
        }
        return true; // 시작 시간 또는 종료 시간이 null인 경우는 유효한 것으로 처리(이후 진행되는 유효성 검사에서 걸릴 예정)
    }
}