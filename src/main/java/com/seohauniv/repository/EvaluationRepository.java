package com.seohauniv.repository;

import com.seohauniv.entity.Evaluation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByEnrollStudentId(String studentId);
    List<Evaluation> findByCourseIdOrderByEnrollStudentIdAsc(String courseId);
    Evaluation findByEnrollStudentIdAndCourseId(String studentId,String courseId);
    List<Evaluation> findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(String studentId,int year,int semester);
    Evaluation findByEnrollId(Long enrollId);
}
