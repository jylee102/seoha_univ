package com.seohauniv.service;

import com.seohauniv.dto.EvaluationFormDto;
import com.seohauniv.entity.Evaluation;
import com.seohauniv.repository.EvaluationRepository;
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
}
