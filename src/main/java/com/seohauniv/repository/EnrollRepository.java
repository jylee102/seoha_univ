package com.seohauniv.repository;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollRepository extends JpaRepository<Enroll, Long> {
    List<Enroll> findByStudent(Student student);
    List<Enroll> findByStudentId(String studentId);

    Page<Enroll> findByCourseId(String courseId, Pageable pageable);

    Enroll findByCourseIdAndStudentId(String courseId, String studentId);

    //현재 로그인한 학생의 수강 신청내역을 페이징 조건에 맞춰서 조회
    Page<Enroll> findByStudentId(String studentId, Pageable pageable);

    List<Enroll> findByCourseAndStudentId(Course course, String id);
}
