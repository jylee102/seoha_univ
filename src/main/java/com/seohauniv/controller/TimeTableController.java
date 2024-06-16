package com.seohauniv.controller;

import com.seohauniv.dto.CourseTimeDto;
import com.seohauniv.entity.Course;
import com.seohauniv.service.EnrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class TimeTableController {
    private final EnrollService enrollService;

    // 학생 시간표 페이지
    @GetMapping(value = "/students/timeTable")
    public String timeTable() {
        return "student/timeTable";
    }

    // 학생의 강의 목록 불러오기
    @RequestMapping("/api/resources")
    @ResponseBody
    public Iterable<CourseTimeDto> resources(Principal principal) {
        try {
            List<Course> courses = enrollService.getCoursesByStudentId(principal.getName());
            List<CourseTimeDto> courseDtos = courses.stream()
                    .flatMap(course -> convertToDto(course).stream()) // Course를 여러 개의 CourseTimeDto로 변환
                    .collect(Collectors.toList());
            return courseDtos;
        } catch (Exception e) {
            e.printStackTrace(); // 디버깅을 위해 예외를 출력
            throw new RuntimeException("강의 정보를 가져오는데 실패하였습니다."); // 예시로 일반적인 에러 응답
        }
    }

    // Course 타입 -> CourseTimeDto 타입
    private List<CourseTimeDto> convertToDto(Course course) {
        return course.getSyllabus().getCourseTimes().stream()
                .map(courseTime -> {
                    CourseTimeDto dto = new CourseTimeDto();
                    dto.setCourseName(course.getSyllabus().getCourseName());
                    dto.setProfessorName(course.getProfessor().getName());
                    dto.setRoomName(course.getRoom().getBuildingName() + " " + course.getRoom().getRoomNo() + "호");

                    dto.setDay(courseTime.getDay());
                    dto.setStartTime(courseTime.getStartTime());
                    dto.setEndTime(courseTime.getEndTime());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
