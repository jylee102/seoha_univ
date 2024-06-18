package com.seohauniv.repository;

import com.seohauniv.entity.Evaluation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    List<Evaluation> findByCourseIdOrderByEnrollStudentIdAsc(String courseId);

    Evaluation findByEnrollStudentIdAndCourseId(String studentId,String courseId);

    List<Evaluation> findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(String studentId,int year,int semester);

    Evaluation findByEnrollId(Long enrollId);

    @Query("SELECT COALESCE(sum(e.course.syllabus.credit),0) from Evaluation e where e.enroll.student.id = :studentId AND e.course.syllabus.year =:year AND  e.course.syllabus.semester = :semester")
    Integer countByCredit(@Param("studentId") String studentId,@Param("year") int year,@Param("semester") int semester);

    @Query("SELECT sum(e.grade)/count(e.course.syllabus.credit) from Evaluation e where e.enroll.student.id = :studentId AND  e.course.syllabus.year =:year AND  e.course.syllabus.semester = :semester")
    float averageGrade(@Param("studentId") String studentId,@Param("year") int year,@Param("semester") int semester);
}
