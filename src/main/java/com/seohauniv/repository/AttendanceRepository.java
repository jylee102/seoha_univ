package com.seohauniv.repository;


import com.seohauniv.constant.AttendStatus;
import com.seohauniv.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    int countByStatusAndStudentId(AttendStatus status, String studentId);
}
