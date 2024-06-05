package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.CourseFormDto;
import com.seohauniv.entity.Course;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.repository.CourseRepository;
import com.seohauniv.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    // 강의 개설
    public Course create(CourseFormDto courseFormDto) {
        courseFormDto.getSyllabus().setStatus(ProcedureStatus.APPROVAL);

        Course course = courseFormDto.toEntity();
        course.setRestSeat(courseFormDto.getSyllabus().getCapacity());

        return courseRepository.save(course);
    }
}
