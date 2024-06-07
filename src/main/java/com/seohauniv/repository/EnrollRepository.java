package com.seohauniv.repository;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface EnrollRepository extends JpaRepository<Enroll, Long> {
    List<Enroll> findByStudent(Student student);
}
