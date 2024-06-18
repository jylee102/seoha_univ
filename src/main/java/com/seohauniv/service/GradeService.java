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
    @Transactional(readOnly = true)
    public int countByCredit(String studentId,int year,int semester){
        Integer count = evaluationRepository.countByCredit(studentId, year, semester);
        return (count != null) ? count : 0;
    }
    @Transactional(readOnly = true)
    public Float averageGrade(String studentId,int year,int semester){
        return evaluationRepository.averageGrade(studentId,year,semester);
    }
}
