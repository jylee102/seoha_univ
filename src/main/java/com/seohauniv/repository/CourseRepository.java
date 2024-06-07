package com.seohauniv.repository;

import com.seohauniv.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CourseRepository extends JpaRepository<Course, String>, QuerydslPredicateExecutor<Course>, CourseRepositoryCustom {
}
