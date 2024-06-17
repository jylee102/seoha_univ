package com.seohauniv.controller;


import com.seohauniv.entity.Evaluation;
import com.seohauniv.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    @GetMapping("/students/grade")
    public String thisGrade(Principal principal, Model model){
        List<Evaluation> grades = gradeService.findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(principal.getName(),2024,1);
        model.addAttribute("grades",grades);
        return "student/thisGrade";
    }
}
