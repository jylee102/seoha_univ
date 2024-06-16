package com.seohauniv.repository;

import com.seohauniv.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String>, QuerydslPredicateExecutor<Course>, CourseRepositoryCustom {

    Page<Course> findByProfessorIdOrderById(String memberId, Pageable pageable);

    Page<Course> findByProfessorIdAndSyllabusYearAndSyllabusSemesterOrderById(String memberId, int year, int semester,Pageable pageable);

    List<Course> findByRoomId(Long roomId);
}
