package com.seohauniv.controller;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Enroll;
import com.seohauniv.service.CourseService;
import com.seohauniv.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ProfessorController {
private final CourseService courseService;
private final ProfessorService professorService;
    @GetMapping(value = "professors/myCourse")
    public String myCourse(Model model, Principal principal){
      List<Course> myCourses= courseService.myCourse(principal.getName());

      model.addAttribute("myCourses",myCourses);
        return "professor/myCourse";
    }
    @GetMapping(value = "professors/course/{courseId}")
    public String myCourseStudent(Model model, @PathVariable("courseId") String courseId) {
        List<Enroll> myCourseStudentList = professorService.getMyCourseStudent(courseId);
        model.addAttribute("students", myCourseStudentList);
        return "professor/myCourseStudent";
    }
}
