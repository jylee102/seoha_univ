package com.seohauniv.repository;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Enroll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String>, QuerydslPredicateExecutor<Course>, CourseRepositoryCustom {
    @Query("SELECT c FROM Course c WHERE c.professor.id = :memberId")
    Page<Course> getCourseById(@Param("memberId") String memberId, Pageable pageable);
    @Query("SELECT c FROM Course c WHERE c.professor.id = :memberId AND c.syllabus.year = :year AND c.syllabus.semester = :semester")
    Page<Course> getCourseByYearAndSemester(@Param("memberId")String memberId,@Param("year")int year,@Param("semester") int semester,Pageable pageable);

    @Query("SELECT e FROM Enroll e WHERE e.course = :course AND e.student.id = :id")
    List<Enroll> findByCourseAndUser(@Param("course") Course course, @Param("id") String id);

}
