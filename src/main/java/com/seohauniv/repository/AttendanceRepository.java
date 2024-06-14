package com.seohauniv.repository;


import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import com.seohauniv.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    int countByStatusAndEnrollIdAndStudentId(AttendStatus status,Long enrollId ,String studentId);
    Attendance findByEnrollIdAndStudentIdAndWeekAndDay(Long enrollId, String studentId, int week, Day day);
    List<Attendance> findByEnrollIdAndWeekAndDay(Long enrollId,int week,Day day);
}
