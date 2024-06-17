package com.seohauniv.service;

import com.seohauniv.dto.EvaluationFormDto;
import com.seohauniv.entity.Evaluation;
import com.seohauniv.repository.EvaluationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EvaluationService {
    private final EvaluationRepository evaluationRepository;

    public Long saveEvaluation(EvaluationFormDto evaluationFormDto){
    Evaluation evaluation = evaluationFormDto.createEvaluation();
    evaluation.calcConvertedScore();
    evaluation.grade();
    evaluationRepository.save(evaluation);

    return evaluation.getId();
    }
    @Transactional(readOnly = true)
    public List<Evaluation> findByEnrollStudentId(String studentId) {
        return evaluationRepository.findByEnrollStudentId(studentId);
    }
    public List<Evaluation> findByCourseIdOrderByEnrollStudentIdAsc(String courseId){
        return evaluationRepository.findByCourseIdOrderByEnrollStudentIdAsc(courseId);
    }
    @Transactional(readOnly = true)
    public EvaluationFormDto updateEvaluationDtl(String studentId,String courseId){
        Evaluation evaluation =evaluationRepository.findByEnrollStudentIdAndCourseId(studentId,courseId);
        return EvaluationFormDto.of(evaluation);
    }
    public Long updateEvaluation(EvaluationFormDto evaluationFormDto){
        Evaluation evaluation = evaluationRepository.findById(evaluationFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        evaluation.updateEvaluation(evaluationFormDto);

        return evaluation.getId();
    }
    @Transactional(readOnly = true)
    public Evaluation findByEnrollId(Long enrollId){
        return evaluationRepository.findByEnrollId(enrollId);
    }
}
