package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.CourseFormDto;
import com.seohauniv.dto.CourseEnrollDto;
import com.seohauniv.dto.CourseSearchDto;
import com.seohauniv.dto.MyCourseSearchDto;
import com.seohauniv.entity.Course;
import com.seohauniv.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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

    @Transactional(readOnly = true)
    public Page<CourseEnrollDto> getEnrollListPage(CourseSearchDto courseSearchDto, Pageable pageable) {
        return courseRepository.getEnrollListPage(courseSearchDto, pageable);
    }

    public Course findById(String id) {
        return courseRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public void deleteRestSeat(Course course) {
        course.setRestSeat(course.getRestSeat() - 1);
    }

    public void updateRestSeat(Course course) {
        course.setRestSeat(course.getRestSeat() + 1);
    }

    @Transactional(readOnly = true)
    public Page<Course> myCourse(String memberId, Pageable pageable){
        return courseRepository.findByProfessorId(memberId, pageable);
    }
    @Transactional(readOnly = true)
    public Page<Course> myCourseSearch(String memberId, MyCourseSearchDto myCourseSearchDto, Pageable pageable){
        return courseRepository.findByProfessorIdAndSyllabusYearAndSyllabusSemester(memberId,myCourseSearchDto.getSearchYear(),myCourseSearchDto.getSearchSemester(),pageable);
    }
}
