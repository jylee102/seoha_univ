package com.seohauniv.validation;

import com.seohauniv.dto.CourseTimeDto;
import com.seohauniv.dto.SyllabusFormDto;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

public class SyllabusFormDtoValidator implements Validator {

    private final Validator courseTimeDtoValidator;

    public SyllabusFormDtoValidator(Validator courseTimeDtoValidator) {
        this.courseTimeDtoValidator = courseTimeDtoValidator;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return SyllabusFormDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SyllabusFormDto syllabusFormDto = (SyllabusFormDto) target;

        // 기본 필드 유효성 검사
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "courseName", "NotBlank", "강좌명은 필수 입력값입니다.");
        if (syllabusFormDto.getCourseType() == null) {
            errors.rejectValue("courseType", "NotNull", "강의 구분은 필수 입력값입니다.");
        }
        if (syllabusFormDto.getCredit() < 1 || syllabusFormDto.getCredit() > 3) {
            errors.rejectValue("credit", "MinMax", "이수학점은 1~3 사이의 숫자이어야 합니다.");
        }

        // CourseTimeDto 리스트 유효성 검사
        List<CourseTimeDto> courseTimes = syllabusFormDto.getCourseTimes();
        if (courseTimes == null || courseTimes.isEmpty()) {
            errors.rejectValue("courseTimes", "NotEmpty", "강의 시간은 필수 입력값입니다.");
        } else {
            for (int i = 0; i < courseTimes.size(); i++) {
                try {
                    errors.pushNestedPath("courseTimes[" + i + "]");
                    ValidationUtils.invokeValidator(courseTimeDtoValidator, courseTimes.get(i), errors);
                } finally {
                    errors.popNestedPath();
                }
            }
        }
    }
}