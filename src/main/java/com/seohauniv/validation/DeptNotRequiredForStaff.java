package com.seohauniv.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DeptNotRequiredForStaffValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DeptNotRequiredForStaff {
    String message() default "학과는 필수 입력값입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}