package com.seohauniv.dto;

import com.seohauniv.constant.AttendStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceFormDto {
    private String studentId;
    private String status;
}
