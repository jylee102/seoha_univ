package com.seohauniv.repository;

import com.seohauniv.entity.Evaluation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    @Query("SELECT e FROM Evaluation e WHERE e.enroll.student.id = :studentId")
    List<Evaluation> getEvaluationsByStudentId(@Param("studentId") String studentId);
}
