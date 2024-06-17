package com.seohauniv.repository;


import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import com.seohauniv.entity.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    int countByStatusAndEnrollIdAndStudentId(AttendStatus status,Long enrollId ,String studentId);
    Attendance findByEnrollIdAndStudentIdAndWeekAndDay(Long enrollId, String studentId, int week, Day day);
    @Query("SELECT a FROM Attendance a WHERE a.enroll.course.id = :courseId AND a.enroll.student.id = :studentId AND a.week = :week AND a.day = :day")
    Attendance findByCourseIdAndWeekAndDay(@Param("courseId") String courseId, @Param("studentId") String studentId, @Param("week") int week, @Param("day") Day day);
    @Query("SELECT a FROM Attendance a WHERE a.enroll.id = :enrollId AND a.week = :week AND a.day = :day")
    Attendance findByEnrollIdAndWeekAndDay(@Param("enrollId") Long enrollId, @Param("week") int week, @Param("day") Day day);
}
