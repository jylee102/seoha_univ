package com.seohauniv.controller;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Student;
import com.seohauniv.service.CourseService;
import com.seohauniv.service.EnrollService;
import com.seohauniv.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EnrollController {
    private final EnrollService enrollService;
    private final StudentService studentService;
    private final CourseService courseService;

    @PostMapping("/student/enrollCourse/{courseId}")
    public @ResponseBody ResponseEntity enrollCourse(@PathVariable("courseId") String courseId, Principal principal) {

        Student student = studentService.findById(principal.getName());
        Course course = courseService.findById(courseId);

        try {
            if (enrollService.checkAlreadyEnrolled(course, student))
                return new ResponseEntity("이미 수강 신청된 강의입니다.", HttpStatus.BAD_REQUEST);
            else if (enrollService.checkTimeConflict(course, student)) {
                return new ResponseEntity("이미 신청한 강의와 시간이 겹칩니다.", HttpStatus.BAD_REQUEST);
            } else {
                enrollService.enroll(student, course);
                courseService.updateRestSeat(course);
                return new ResponseEntity("수강신청이 완료되었습니다.", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("수강신청에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
