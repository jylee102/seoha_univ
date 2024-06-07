package com.seohauniv.repository;

import com.seohauniv.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String>, QuerydslPredicateExecutor<Course>, CourseRepositoryCustom {
    @Query("SELECT c FROM Course c WHERE c.professor.id = :memberId")
    List<Course> getCourseById(@Param("memberId") String memberId);
}
