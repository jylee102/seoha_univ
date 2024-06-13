package com.seohauniv.service;

import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import com.seohauniv.dto.AttendanceFormDto;
import com.seohauniv.entity.*;
import com.seohauniv.repository.AttendanceRepository;
import com.seohauniv.repository.EvaluationRepository;
import com.seohauniv.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    public Attendance addAttendance(String studentId, String status, String day,int week) {
        Attendance attendance = new Attendance();
        Student student = studentRepository.findById(studentId).orElseThrow(EntityNotFoundException::new);

        if(status.equals("PRESENT")){
            attendance.setStudent(student);
            attendance.setStatus(AttendStatus.PRESENT);
            attendance.setDay(Day.valueOf(day));
            attendance.setWeek(week);
        } else if(status.equals("LATE")){
            attendance.setStudent(student);
            attendance.setStatus(AttendStatus.LATE);
            attendance.setDay(Day.valueOf(day));
            attendance.setWeek(week);
        } else {
            attendance.setStudent(student);
            attendance.setStatus(AttendStatus.ABSENT);
            attendance.setDay(Day.valueOf(day));
            attendance.setWeek(week);
        }

        return attendanceRepository.save(attendance);
    }
    @Transactional(readOnly = true)
    public int countByStatusAndStudentId(AttendStatus status,String studentId) {
        return attendanceRepository.countByStatusAndStudentId(status,studentId);
    }

}
