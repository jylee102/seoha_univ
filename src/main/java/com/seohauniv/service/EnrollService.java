package com.seohauniv.service;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Student;
import com.seohauniv.repository.EnrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollService {
    private final EnrollRepository enrollRepository;
    private final CourseService courseService;

    public boolean checkAlreadyEnrolled(Course course, Student student) {
        List<Enroll> enrolls = enrollRepository.findByStudent(student);
        for (Enroll enroll : enrolls) {
            if (course.equals(enroll.getCourse())) return true;
        }

        return false;
    }

    public boolean checkTimeConflict(Course course, Student student) {
        List<Enroll> enrolls = enrollRepository.findByStudent(student);

        for (Enroll enroll : enrolls) {
            for (CourseTime courseTime : enroll.getCourse().getSyllabus().getCourseTimes()) { // 이미 신청한 강의의 강의시간
                for (CourseTime time : course.getSyllabus().getCourseTimes()) { // 신청하려는 강의의 강의시간
                    if (time.getDay().equals(courseTime.getDay())) {
                        // 시간 범위가 겹치는지 확인
                        if (!(time.getEndTime().isBefore(courseTime.getStartTime()) || time.getStartTime().isAfter(courseTime.getEndTime()))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Enroll enroll(Student student, Course course) {
        Enroll enroll = new Enroll();
        enroll.setCourse(course);
        enroll.setStudent(student);

        return enrollRepository.save(enroll);
    }
}
