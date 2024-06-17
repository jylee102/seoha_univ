package com.seohauniv.controller;

import com.seohauniv.dto.CourseFormDto;
import com.seohauniv.dto.CourseEnrollDto;
import com.seohauniv.dto.CourseSearchDto;
import com.seohauniv.entity.Course;
import com.seohauniv.entity.Message;
import com.seohauniv.service.CourseService;
import com.seohauniv.service.EnrollService;
import com.seohauniv.service.MessageService;
import com.seohauniv.service.RoomService;
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

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final EnrollService enrollService;
    private final RoomService roomService;
    private final MessageService messageService;

    // 강의 개설
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
            return "redirect:/staff/viewSyllabus/" + courseFormDto.getSyllabus().getId(); // 리다이렉트하여 페이지를 리로드
        }

        try {

            if (!roomService.checkTimeConflict(courseFormDto)) {
                Course course = courseService.create(courseFormDto); // 강의 개설

                // 해당 강의의 계획서를 제출한 교수에게 메시지
                Message message = new Message(course);
                messageService.create(message);

                redirectAttributes.addFlashAttribute("message", "해당 강의가 개설되었습니다.");
                return "redirect:/staffs/createCourse";
            } else {
                redirectAttributes.addFlashAttribute("message", "해당 시간에 강의실을 이용할 수 없습니다.");
                return "redirect:/staff/viewSyllabus/" + courseFormDto.getSyllabus().getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "강의 개설 도중 문제가 발생했습니다.");
            return "redirect:/staff/viewSyllabus/" + courseFormDto.getSyllabus().getId();
        }
    }

    // 수강신청 페이지
    @GetMapping(value = {"/students/enroll", "/students/enroll/{page}"})
    public String enrollList(CourseSearchDto courseSearchDto,
                             @PathVariable("page") Optional<Integer> page,
                             Model model, Principal principal, RedirectAttributes redirectAttributes) {

        try {
            //URL path에 페이지가 있으면 해당 페이지 번호를 조회하고, 페이지 번호가 없다면 0 페이지(첫번째 페이지)를 조회
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

            Page<CourseEnrollDto> coursesPage = courseService.getEnrollListPage(courseSearchDto, pageable);
            String memberId = principal.getName(); // 현재 로그인한 학생 ID를 가져온다.

            // 각 강의에 대해 현재 로그인한 학생이 수강신청했는지 확인
            Map<String, Boolean> enrollStatus = new HashMap<>();
            for (CourseEnrollDto courseEnrollDto : coursesPage) {
                Course course = courseService.findById(courseEnrollDto.getId());
                boolean isEnrolled = enrollService.isAlreadyEnrolled(course, memberId);
                enrollStatus.put(course.getId(), isEnrolled);
            }

            model.addAttribute("courses", coursesPage);
            model.addAttribute("courseSearchDto", courseSearchDto);
            model.addAttribute("enrollStatus", enrollStatus);
            model.addAttribute("maxPage", 5);

            return "student/enrollList";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "강의 목록 불러오기에 실패했습니다.");
            return "redirect:/";
        }
    }

    // 강의 계획서 PDF 보기
    @GetMapping("/course/syllabus/{courseId}")
    public String viewPDF(@PathVariable("courseId") String courseId, Model model) {
        Course course = courseService.findById(courseId);
        model.addAttribute("pdfURL", course.getPdf());
        return "professor/readSyllabus";
    }
}
