package com.seohauniv.controller;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Student;
import com.seohauniv.service.CourseService;
import com.seohauniv.service.EnrollService;
import com.seohauniv.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class EnrollController {
    private final EnrollService enrollService;
    private final StudentService studentService;
    private final CourseService courseService;

    // 수강 신청
    @PostMapping(value = "/student/enrollCourse/{courseId}")
    public @ResponseBody ResponseEntity enrollCourse(@PathVariable("courseId") String courseId, Principal principal) {

        try {
            Student student = studentService.findById(principal.getName());
            Course course = courseService.findById(courseId);

            if (course.getRestSeat() <= 0)
                return new ResponseEntity("수강 신청 가능한 인원이 초과되었습니다.", HttpStatus.BAD_REQUEST);
            else if (enrollService.checkAlreadyEnrolled(course, student))
                return new ResponseEntity("이미 수강 신청된 강의입니다.", HttpStatus.BAD_REQUEST);
            else if (enrollService.checkTimeConflict(course, student))
                return new ResponseEntity("이미 신청한 강의와 시간이 겹칩니다.", HttpStatus.BAD_REQUEST);
            else {
                enrollService.enroll(student, course);
                courseService.deleteRestSeat(course);
                return new ResponseEntity("수강 신청이 완료되었습니다.", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("수강 신청에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 내 강의 목록 페이지
    @GetMapping(value = {"/students/myCourse", "/students/myCourse/{page}"})
    public String myCourseList (@PathVariable("page")Optional<Integer> page,
                                Principal principal, Model model, RedirectAttributes redirectAttributes) {

        try {
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
            Page<Enroll> myEnrollDtoPList = enrollService.getMyEnrollList(principal.getName(), pageable);

            model.addAttribute("enrolls", myEnrollDtoPList);
            model.addAttribute("maxPage", 5);

            return "student/myCourseList";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "내 강의 목록 불러오기에 실패했습니다.\n관리자에게 문의해주세요.");
            return "redirect:/";
        }
    }

    // 수강신청 취소(내 강의)
    @DeleteMapping("/students/myCourse/{enrollId}/delete")
    public @ResponseBody ResponseEntity deleteMyEnroll(@PathVariable("enrollId") Long enrollId,
                                                     @RequestBody Map<String, String> map, Principal principal) {

        try {
            String courseId = map.get("courseId");
            Course course = courseService.findById(courseId);

            if (!enrollService.validateEnroll(enrollId, principal.getName())) {
                return new ResponseEntity<String>("수강신청 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }

            enrollService.deleteEnroll(enrollId);
            courseService.updateRestSeat(course);

            return new ResponseEntity<>(enrollId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("수강취소에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    // 수강신청 취소(수강신청)
    @DeleteMapping(value = "/students/enroll/{courseId}/delete")
    public @ResponseBody ResponseEntity<?> deleteEnroll(@PathVariable("courseId") String courseId, Principal principal) {
        try {
            Course course = courseService.findById(courseId);

            Enroll enroll = enrollService.findByStudentIdAndCourseId(courseId, principal.getName());

            if (!enrollService.validateEnroll(enroll.getId(), principal.getName())) {
                return new ResponseEntity<String>("수강신청 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }

            enrollService.deleteEnroll(enroll.getId());
            courseService.updateRestSeat(course);

            return new ResponseEntity<>(enroll.getId(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("수강취소에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
