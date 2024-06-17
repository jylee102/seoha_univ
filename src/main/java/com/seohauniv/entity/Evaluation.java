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

    private float grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enroll_id")
    private Enroll enroll;

    public void calcConvertedScore(){
        this.convertedScore = (0.2F*homework)+(0.3F*midExam)+(0.5F*finalExam);
    }
    public void grade(){
       convertedScore =(0.2F*homework)+(0.3F*midExam)+(0.5F*finalExam);
        if(convertedScore >= 95){
            this.grade = 4.5F;
        } else if (convertedScore <95 && convertedScore>=90) {
            this.grade = 4.0F;
        } else if (convertedScore <90 && convertedScore>=85) {
            this.grade = 3.5F;
        } else if (convertedScore <85 && convertedScore>=80) {
            this.grade = 3.0F;
        } else if (convertedScore <80 && convertedScore>=75) {
            this.grade = 2.5F;
        } else if (convertedScore <75 && convertedScore>=70) {
            this.grade = 2.0F;
        } else if (convertedScore <70 && convertedScore>=65) {
            this.grade = 1.5F;
        } else if (convertedScore <65 && convertedScore>=60) {
            this.grade = 1.0F;
        } else {
            this.grade = 0.0F;
        }
    }
    public void updateEvaluation(EvaluationFormDto evaluationFormDto){
        this.homework = evaluationFormDto.getHomework();
        this.midExam = evaluationFormDto.getMidExam();
        this.finalExam = evaluationFormDto.getFinalExam();
        this.convertedScore = (0.2F*homework)+(0.3F*midExam)+(0.5F*finalExam);
        if(convertedScore >= 95){
            this.grade = 4.5F;
        } else if (convertedScore <95 && convertedScore>=90) {
            this.grade = 4.0F;
        } else if (convertedScore <90 && convertedScore>=85) {
            this.grade = 3.5F;
        } else if (convertedScore <85 && convertedScore>=80) {
            this.grade = 3.0F;
        } else if (convertedScore <80 && convertedScore>=75) {
            this.grade = 2.5F;
        } else if (convertedScore <75 && convertedScore>=70) {
            this.grade = 2.0F;
        } else if (convertedScore <70 && convertedScore>=65) {
            this.grade = 1.5F;
        } else if (convertedScore <65 && convertedScore>=60) {
            this.grade = 1.0F;
        } else {
            this.grade = 0.0F;
        }
    }
}
