package com.seohauniv.dto;

import com.seohauniv.entity.Evaluation;
import com.seohauniv.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class EvaluationFormDto {
    private Long id;

    private String courseId;

    private Long enrollId;

    private int homework;

    private int midExam;

    private int finalExam;

    private static ModelMapper modelMapper = new ModelMapper();

    public Evaluation createEvaluation() {
        return modelMapper.map(this, Evaluation.class);
    }

    public static EvaluationFormDto of(Evaluation evaluation) {
        return modelMapper.map(evaluation, EvaluationFormDto.class);
    }
}
