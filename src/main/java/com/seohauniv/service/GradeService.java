package com.seohauniv.service;

import com.seohauniv.entity.Evaluation;
import com.seohauniv.repository.EvaluationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeService {
    private final EvaluationRepository evaluationRepository;
    @Transactional(readOnly = true)
    public List<Evaluation> findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(String studentId,int year,int semester){
        return evaluationRepository.findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(studentId,year,semester);
    }
}
