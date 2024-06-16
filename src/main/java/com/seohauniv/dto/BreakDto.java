package com.seohauniv.dto;

import com.seohauniv.constant.LeaveReason;
import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Dept;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BreakDto {
    private Long id;

    private int grade;

    private int fromYear;

    private int fromSemester;

    private int toYear;

    private int toSemester;

    private LocalDateTime regDate;

    private LocalDateTime updateDate;

    @NotNull(message = "휴학구분은 필수 선택입니다.")
    private LeaveReason reasonType;

    private ProcedureStatus status;

    private String studentName;
    private String studentId;
    private Dept dept;

    public BreakDto(Break b){
        this.id = b.getId();
        this.status = b.getStatus();
        this.toSemester = b.getToSemester();
        this.toYear = b.getToYear();
        this.fromSemester = b.getFromSemester();
        this.fromYear = b.getFromYear();
        this.grade = b.getStudentGrade();
        this.reasonType = b.getReasonType();
        this.regDate = b.getRegDate();
        this.updateDate = b.getUpdateDate();
        this.studentId = b.getStudent().getId();
        this.studentName = b.getStudent().getName();
        this.dept = b.getStudent().getMajor();
    }
}
