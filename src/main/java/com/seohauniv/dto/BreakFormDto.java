package com.seohauniv.dto;

import com.seohauniv.constant.LeaveReason;
import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Student;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BreakFormDto {
    private Student student;

    private Long id;

    private int grade;

    private int fromYear;

    private int fromSemester;

    private int toYear;

    private int toSemester;

    @NotNull(message = "휴학구분은 필수 선택입니다.")
    private LeaveReason reasonType;

    private ProcedureStatus status = ProcedureStatus.PROCESSING;

    private Break aBreak;

    private static ModelMapper modelMapper = new ModelMapper();

    public Break creatBreak() {
        Break breakEntity = modelMapper.map(this, Break.class);
        breakEntity.setStatus(ProcedureStatus.PROCESSING); // 상태 값을 처리중으로 설정
        return breakEntity;
    }
    public static BreakFormDto of(Break breaks) {
        return modelMapper.map(breaks, BreakFormDto.class);
    }
}
