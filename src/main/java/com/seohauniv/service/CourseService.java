package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.CourseFormDto;
import com.seohauniv.dto.CourseEnrollDto;
import com.seohauniv.dto.CourseSearchDto;
import com.seohauniv.dto.MyCourseSearchDto;
import com.seohauniv.entity.Course;
import com.seohauniv.entity.Room;
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
    private final PdfService pdfService;

    @Transactional(readOnly = true)
    public Course findById(String id) {
        return courseRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // 강의 개설
    public Course create(CourseFormDto courseFormDto) throws Exception {
        courseFormDto.getSyllabus().setStatus(ProcedureStatus.APPROVAL);

        Course course = courseFormDto.toEntity();
        course.setRestSeat(courseFormDto.getSyllabus().getCapacity());

        String pdf = pdfService.makePdf(course); // PDF 생성
        course.setPdf("/pdfs/pdf/" + pdf);

        return courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public Page<CourseEnrollDto> getEnrollListPage(CourseSearchDto courseSearchDto, Pageable pageable) {
        return courseRepository.getEnrollListPage(courseSearchDto, pageable);
    }

    public void deleteRestSeat(Course course) {
        course.setRestSeat(course.getRestSeat() - 1);
    }

    public void updateRestSeat(Course course) {
        course.setRestSeat(course.getRestSeat() + 1);
    }

    @Transactional(readOnly = true)
    public Page<Course> myCourse(String memberId, Pageable pageable){
        return courseRepository.findByProfessorIdOrderById(memberId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Course> myCourseSearch(String memberId, MyCourseSearchDto myCourseSearchDto, Pageable pageable){
        return courseRepository.findByProfessorIdAndSyllabusYearAndSyllabusSemesterOrderById(memberId,myCourseSearchDto.getSearchYear(),myCourseSearchDto.getSearchSemester(),pageable);
    }

    @Transactional(readOnly = true)
    public List<Course> findByRoomId(Room room) {
        return courseRepository.findByRoomId(room.getId());
    }

}
