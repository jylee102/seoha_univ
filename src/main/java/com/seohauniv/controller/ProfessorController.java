package com.seohauniv.controller;

import com.seohauniv.entity.Course;
import com.seohauniv.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfessorController {
private final CourseService courseService;
    @GetMapping(value = "professors/myCourse")
    public String myCourse(Model model, Principal principal){
      List<Course> myCourses= courseService.myCourse(principal.getName());

      model.addAttribute("myCourses",myCourses);
        return "professor/myCourse";
    }
}
