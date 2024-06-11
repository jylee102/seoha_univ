package com.seohauniv.service;

import com.seohauniv.constant.Day;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Professor;
import com.seohauniv.repository.CourseTimeRepository;
import com.seohauniv.repository.EnrollRepository;
import com.seohauniv.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    public List<Map<String, Object>> getCourseEventsByDay(Day day) {
        // 해당 요일에 해당하는 강의 시간표를 DB에서 조회하는 코드 작성
        List<CourseTime> courseTimes = courseTimeRepository.findByDay(day);

        // 각 강의 시간표를 FullCalendar의 이벤트 객체로 변환하여 리스트에 추가
        List<Map<String, Object>> events = new ArrayList<>();
        for (CourseTime courseTime : courseTimes) {
            Map<String, Object> event = new HashMap<>();
            event.put("title", courseTime.getSyllabus().getCourseName()); // 강의명을 이벤트 제목으로 설정
            event.put("start", courseTime.getStartTime()); // 강의 시작 시간을 이벤트 시작 시간으로 설정
            event.put("end", courseTime.getEndTime()); // 강의 종료 시간을 이벤트 종료 시간으로 설정
            events.add(event);
        }

        return events;
    }
}
