package com.seohauniv.controller;


import com.seohauniv.dto.MyCourseSearchDto;
import com.seohauniv.dto.TotalGradeDto;
import com.seohauniv.entity.Evaluation;
import com.seohauniv.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GradeController {
    private final GradeService gradeService;
    @GetMapping("/students/grade")
    public String thisGrade(Principal principal, Model model){
        try{
        List<Evaluation> grades = gradeService.findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(principal.getName(),2024,1);
        int countByGrade = gradeService.countByCredit(principal.getName(),2024,1);
        float averageGrade = gradeService.averageGrade(principal.getName(), 2024,1);

            TotalGradeDto totalGradeDto =new TotalGradeDto();
            totalGradeDto.setYear(2024);
            totalGradeDto.setSemester(1);
            totalGradeDto.setSumGrades(countByGrade);
            totalGradeDto.setAverage(averageGrade);

        model.addAttribute("grades",grades);
        model.addAttribute("totalGradeDto",totalGradeDto);
        return "student/thisGrade";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("grades", new ArrayList<Evaluation>());
            model.addAttribute("totalGradeDto", null);
            return "student/thisGrade";
        }
    }
    @GetMapping("/students/gradeFor")
    public String gradeFor(@ModelAttribute MyCourseSearchDto myCourseSearchDto, Principal principal, Model model,
                           @RequestParam(value = "searchYear", defaultValue = "2024") int year, @RequestParam(value = "searchSemester", defaultValue = "1") int semester){

        try {
            List<Evaluation> grades = gradeService.findByEnrollStudentIdAndCourseSyllabusYearAndCourseSyllabusSemester(principal.getName(), year, semester);
            int countByGrade = gradeService.countByCredit(principal.getName(), year, semester);
            float averageGrade = gradeService.averageGrade(principal.getName(), year, semester);

            TotalGradeDto totalGradeDto = new TotalGradeDto();
            totalGradeDto.setYear(year);
            totalGradeDto.setSemester(semester);
            totalGradeDto.setSumGrades(countByGrade);
            totalGradeDto.setAverage(averageGrade);

            model.addAttribute("grades", grades);
            model.addAttribute("myCourseSearchDto", myCourseSearchDto);
            model.addAttribute("totalGradeDto", totalGradeDto);
            return "student/gradeFor";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("grades", new ArrayList<Evaluation>());
            model.addAttribute("myCourseSearchDto", myCourseSearchDto);
            model.addAttribute("totalGradeDto", null);
            return "student/gradeFor";
        }
    }
}
