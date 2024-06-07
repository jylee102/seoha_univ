package com.seohauniv.validation;

import com.seohauniv.dto.MemberFormDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DeptNotRequiredForStaffValidator implements ConstraintValidator<DeptNotRequiredForStaff, MemberFormDto> {

    @Override
    public void initialize(DeptNotRequiredForStaff constraintAnnotation) {
    }

    @Override
    public boolean isValid(MemberFormDto memberFormDto, ConstraintValidatorContext context) {
        if ("STAFF".equals(memberFormDto.getRole())) {
            return true;
        } else {
            if (memberFormDto.getDept() == null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("학과는 필수 입력값입니다.")
                        .addPropertyNode("dept")
                        .addConstraintViolation();
                return false;
            }
            return true;
        }
    }
}