package com.seohauniv.controller;

import com.seohauniv.dto.MyCourseSearchDto;
import com.seohauniv.entity.Course;
import com.seohauniv.entity.Enroll;
import com.seohauniv.service.CourseService;
import com.seohauniv.service.ProfessorService;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class ProfessorController {
private final CourseService courseService;
private final ProfessorService professorService;
    @GetMapping(value = {"professors/myCourse","/professors/myCourse/{page}"})
    public String myCourse(@ModelAttribute MyCourseSearchDto myCourseSearchDto, Model model, Principal principal, @PathVariable("page") Optional<Integer> page){
        int searchYear = myCourseSearchDto.getSearchYear() != 0 ? myCourseSearchDto.getSearchYear() : -1;
        int searchSemester = myCourseSearchDto.getSearchSemester() != 0 ? myCourseSearchDto.getSearchSemester() : -1;

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<Course> myCourses;
        if(searchYear == -1 && searchSemester ==-1){
            myCourses =courseService.myCourse(principal.getName(),pageable);
        } else {
         myCourses = courseService.myCourseSearch(principal.getName(), myCourseSearchDto, pageable);
        }
        model.addAttribute("myCourses",myCourses);
        model.addAttribute("myCourseSearchDto", myCourseSearchDto);
        model.addAttribute("maxPage", 5);
        return "professor/myCourse";
    }
    @GetMapping(value = "professors/course/{courseId}")
    public String myCourseStudent(Model model, @PathVariable("courseId") String courseId) {
        List<Enroll> myCourseStudentList = professorService.getMyCourseStudent(courseId);
        model.addAttribute("students", myCourseStudentList);

        return "professor/myCourseStudent";
    }
}
