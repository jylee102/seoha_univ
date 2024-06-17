package com.seohauniv.entity;

import com.seohauniv.dto.EvaluationFormDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "evaluation")
@Data
public class Evaluation {
    @Id
    @Column(name = "evaluation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int homework;
    private int midExam;
    private int finalExam;

    @Column(name = "converted_score")
    private float convertedScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enroll_id")
    private Enroll enroll;

    public void calcConvertedScore(){
        this.convertedScore = (0.2F*homework)+(0.3F*midExam)+(0.5F*finalExam);
    }

    public void updateEvaluation(EvaluationFormDto evaluationFormDto){
        this.homework = evaluationFormDto.getHomework();
        this.midExam = evaluationFormDto.getMidExam();
        this.finalExam = evaluationFormDto.getFinalExam();
        this.convertedScore = (0.2F*evaluationFormDto.getHomework())+(0.3F*evaluationFormDto.getMidExam())+(0.5F*evaluationFormDto.getFinalExam());
    }
}
