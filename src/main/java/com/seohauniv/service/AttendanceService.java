package com.seohauniv.service;

import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import com.seohauniv.dto.AttendanceFormDto;
import com.seohauniv.entity.*;
import com.seohauniv.repository.AttendanceRepository;
import com.seohauniv.repository.EnrollRepository;
import com.seohauniv.repository.EvaluationRepository;
import com.seohauniv.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final EnrollRepository enrollRepository;

    public Attendance addAttendance(String studentId, String courseId, String status, String day, int week) {
        Attendance attendance = new Attendance();
        Student student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);
        Enroll enroll = enrollRepository.findByCourseIdAndStudentId(courseId, studentId);

        if (status.equals("PRESENT")) {
            attendance.setStudent(student);
            attendance.setStatus(AttendStatus.PRESENT);
            attendance.setDay(Day.valueOf(day));
            attendance.setEnroll(enroll);
            attendance.setWeek(week);
        } else if (status.equals("LATE")) {
            attendance.setStudent(student);
            attendance.setStatus(AttendStatus.LATE);
            attendance.setDay(Day.valueOf(day));
            attendance.setEnroll(enroll);
            attendance.setWeek(week);
        } else {
            attendance.setStudent(student);
            attendance.setStatus(AttendStatus.ABSENT);
            attendance.setDay(Day.valueOf(day));
            attendance.setEnroll(enroll);
            attendance.setWeek(week);
        }

        return attendanceRepository.save(attendance);
    }

    @Transactional(readOnly = true)
    public int countByStatusAndStudentId(AttendStatus status, Long enrollId, String studentId) {
        return attendanceRepository.countByStatusAndEnrollIdAndStudentId(status, enrollId, studentId);
    }
    @Transactional(readOnly = true)
    public Page<Attendance> findByEnrollIdAndStudentIdAndWeekAndDay(String courseId, int week, Day day,Pageable pageable){
        return attendanceRepository.findByCourseIdAndWeekAndDay(courseId,week,day,
        pageable);
    };
}
