package com.seohauniv.controller;

import com.seohauniv.dto.CourseFormDto;
import com.seohauniv.dto.CourseEnrollDto;
import com.seohauniv.dto.CourseSearchDto;
import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/staff/course/create")
    public String createCourse(@ModelAttribute @Valid CourseFormDto courseFormDto, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            //유효성 체크후 에러결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n"); //에러메세지를 가지고온다.
            }

            redirectAttributes.addFlashAttribute("message", sb.toString());
            return "redirect:/staff/viewSyllabus/" + courseFormDto.getSyllabus().getId(); // 리다이렉트하여 페이지를 리로드합니다.
        }

        try {
            courseService.create(courseFormDto);

            redirectAttributes.addFlashAttribute("message", "해당 강의가 개설되었습니다.");
            return "redirect:/staff/viewSyllabus/" + courseFormDto.getSyllabus().getId(); // 리다이렉트하여 페이지를 리로드합니다.
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "강의 개설 도중 문제가 발생했습니다.");
            return "redirect:/staff/viewSyllabus/" + courseFormDto.getSyllabus().getId(); // 리다이렉트하여 페이지를 리로드합니다.
        }
    }

    // 수강신청 페이지
    @GetMapping(value = {"/students/enroll", "/students/enroll/{page}"})
    public String enrollList(CourseSearchDto courseSearchDto,
                             @PathVariable("page") Optional<Integer> page, Model model) {

        //PageRequest.of(페이지 번호, 한 페이지당 조회할 데이터 갯수);
        //URL path에 페이지가 있으면 해당 페이지 번호를 조회하고, 페이지 번호가 없다면 0 페이지(첫번째 페이지)를 조회
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 2);

        Page<?> courses = courseService.getEnrollListPage(courseSearchDto, pageable);

        System.out.println(courses);

        model.addAttribute("courses", courses);
        model.addAttribute("courseSearchDto", courseSearchDto);
        model.addAttribute("maxPage", 5);

        return "student/enrollList";
    }
}
