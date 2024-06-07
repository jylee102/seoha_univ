package com.seohauniv.dto;

import com.seohauniv.constant.LeaveReason;
import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Student;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class BreakFormDto {
    private Student student;

    private String memberId;

    private Long id;

//    private int studentGrade;

    private int fromYear;

    private int fromSemester;

    private int toYear;

    private int toSemester;

    private LeaveReason reasonType;

    private ProcedureStatus status;

    private static ModelMapper modelMapper = new ModelMapper();

    public Break creatBreak(){
        return modelMapper.map(this, Break.class);
    }
    //postFormDto 클래스의 객체를 기반으로 post 객체를 생성한다. modelMapper 를 사용하여 객체 간의 매핑을 수행함
    //postFormDto => post 엔티티로 변환

    public static BreakFormDto of(Break breaks) {
        return modelMapper.map(breaks, BreakFormDto.class);
    }
    //post 객체를 postFromDto 객체로 변환



}
