package com.seohauniv.repository;

import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrollRepository extends JpaRepository<Enroll, Long> {
    List<Enroll> findByStudent(Student student);

    @Query("SELECT e FROM Enroll e WHERE e.course.id = :courseId")
    List<Enroll> findStudentsByCourseId(@Param("courseId") String courseId, Pageable pageable);
    @Query("SELECT count(e) FROM Enroll e WHERE e.course.id = :courseId")
    Long findStudentsByCourseId(@Param("courseId") String courseId);

    @Query("SELECT e FROM Enroll e WHERE e.course.id = :courseId AND e.student.id= :studentId")
    Enroll findStudentsByCourseIdAndStudentId(@Param("courseId") String courseId,@Param("studentId") String studentId);


    //현재 로그인한 학생의 수강 신청내역을 페이징 조건에 맞춰서 조회
    @Query("select e from Enroll e where e.student.id = :memberId")
    List<Enroll> findEnrolls(@Param("memberId") String memberId, Pageable pageable);

    //현재 로그인한 학생의 수강 신청 갯수가 몇개인지 조회(total)
    @Query("select count(e) from Enroll e where e.student.id = :memberId")
    Long countEnroll(@Param("memberId") String memberId);

}
