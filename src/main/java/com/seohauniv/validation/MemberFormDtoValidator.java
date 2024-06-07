package com.seohauniv.validation;

import com.seohauniv.dto.MemberFormDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MemberFormDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberFormDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberFormDto memberFormDto = (MemberFormDto) target;

        // 필수 필드 검증
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required", "이름은 필수 입력 값입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required", "이메일은 필수 입력 값입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "birth", "field.required", "생일은 필수 입력 값입니다.");

        // 추가적인 검증 로직
        if (!memberFormDto.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z]{2,6}$")) {
            errors.rejectValue("email", "field.invalid", "이메일 형식으로 입력해주세요.");
        }
        if (!memberFormDto.getPhone().matches("^\\d{3}-\\d{3,4}-\\d{4}$")) {
            errors.rejectValue("phone", "field.invalid", "전화번호 형식이 올바르지 않습니다.");
        }

        // 커스텀 어노테이션 검증 로직
        if (!memberFormDto.getRole().equals("STAFF") && memberFormDto.getDept() == null) {
            errors.rejectValue("dept", "field.invalid", "학생과 교수는 학과가 필수 입력값입니다.");
        }
    }
}
