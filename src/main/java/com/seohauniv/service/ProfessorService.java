package com.seohauniv.service;

import com.seohauniv.constant.Day;
import com.seohauniv.dto.AttendanceWeekListDto;
import com.seohauniv.entity.*;
import com.seohauniv.repository.CourseRepository;
import com.seohauniv.repository.CourseTimeRepository;
import com.seohauniv.repository.EnrollRepository;
import com.seohauniv.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final EnrollRepository enrollRepository;
    private final CourseRepository courseRepository;
    private final CourseTimeRepository courseTimeRepository;
    @Transactional(readOnly = true)
    public Professor findById(String id) {
        return professorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Page<Enroll> getMyCourseStudentList(String courseId, Pageable pageable) {
        return enrollRepository.findByCourseId(courseId, pageable);
    }

    @Transactional(readOnly = true)
    public Enroll findStudentsByCourseIdAndStudentId(String courseId,String studentId) {
        return enrollRepository.findByCourseIdAndStudentId(courseId,studentId);
    }
    public Page<AttendanceWeekListDto> getAttendancePage(Course course, Pageable pageable) {
        List<AttendanceWeekListDto> attendanceWeekListDtos = new ArrayList<>();
        for (WeeklyPlan weeklyPlan : course.getSyllabus().getWeeklyPlans()) {
            for (CourseTime courseTime : course.getSyllabus().getCourseTimes()) {
                AttendanceWeekListDto attendanceWeekListDto = new AttendanceWeekListDto();
                attendanceWeekListDto.setWeek(weeklyPlan.getWeek());
                attendanceWeekListDto.setCourseName(course.getSyllabus().getCourseName());
                attendanceWeekListDto.setCourseTime(courseTime);
                attendanceWeekListDtos.add(attendanceWeekListDto);
            }
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), attendanceWeekListDtos.size());
        Page<AttendanceWeekListDto> attendancePage = new PageImpl<>(attendanceWeekListDtos.subList(start, end), pageable, attendanceWeekListDtos.size());
        return attendancePage;
    }
}
