package com.seohauniv.controller;

import com.seohauniv.constant.AttendStatus;
import com.seohauniv.constant.Day;
import com.seohauniv.dto.*;
import com.seohauniv.entity.*;
import com.seohauniv.service.AttendanceService;
import com.seohauniv.service.CourseService;
import com.seohauniv.service.EvaluationService;
import com.seohauniv.service.ProfessorService;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class ProfessorController {
    private final AttendanceService attendanceService;
    private final CourseService courseService;
    private final ProfessorService professorService;
    private final EvaluationService evaluationService;

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
    @GetMapping(value = {"professors/course/{courseId}","professors/course/{courseId}/{page}"})
    public String myCourseStudent(Model model, @PathVariable("courseId") String courseId,@PathVariable("page") Optional<Integer> page) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 5);
        Page<Enroll> myCourseStudentList = professorService.getMyCourseStudentList(courseId,pageable);
        List<StudentAttendanceDto> studentAttendanceDto = new ArrayList<>();

        for (Enroll enroll : myCourseStudentList) {
            int countStatusPresent = attendanceService.countByStatusAndStudentId(AttendStatus.PRESENT, enroll.getId(), enroll.getStudent().getId());
            int countStatusLate = attendanceService.countByStatusAndStudentId(AttendStatus.LATE,enroll.getId(),enroll.getStudent().getId());
            int countStatusAbsent = attendanceService.countByStatusAndStudentId(AttendStatus.ABSENT,enroll.getId(),enroll.getStudent().getId());
            Evaluation evaluation = evaluationService.findByEnrollId(enroll.getId());
            Course course = courseService.findById(courseId);
            StudentAttendanceDto attendanceDto = new StudentAttendanceDto();
            attendanceDto.setStudent(enroll.getStudent());
            attendanceDto.setCountPresent(countStatusPresent);
            attendanceDto.setCountLate(countStatusLate);
            attendanceDto.setCountAbsent(countStatusAbsent);
            attendanceDto.setCourse(course);
            if(evaluation != null){attendanceDto.setEvaluation(evaluation);}
            studentAttendanceDto.add(attendanceDto);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), studentAttendanceDto.size());
        Page<StudentAttendanceDto> studentAttendance = new PageImpl<>(studentAttendanceDto.subList(start, end), pageable, studentAttendanceDto.size());
        model.addAttribute("students",studentAttendance);
        model.addAttribute("maxPage", 5);

        return "professor/myCourseStudent";
    }

    @GetMapping(value = "professors/evaluation/{courseId}/{studentId}")
    public String evaluation(Model model, @PathVariable("courseId") String courseId,@PathVariable("studentId")String studentId){
        Enroll enroll = professorService.findStudentsByCourseIdAndStudentId(courseId,studentId);
        model.addAttribute("enroll",enroll);
        model.addAttribute("evaluationFormDto",new EvaluationFormDto());
        return "professor/evaluation";
    }
    @PostMapping(value = "professors/evaluation/{courseId}/{studentId}")
    public String saveEvaluation(EvaluationFormDto evaluationFormDto, RedirectAttributes redirectAttributes, @PathVariable("studentId")String studentId){
        try {
            List<Evaluation> evaluations = evaluationService.findByEnrollStudentId(studentId);
            if(!evaluations.isEmpty()){
                redirectAttributes.addFlashAttribute("errorMessage","이미 기입된 기록이 있습니다.");
                return "redirect:/professors/myCourse";
            }
            evaluationService.saveEvaluation(evaluationFormDto);
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage","오류가 발생했습니다.");
            return "professor/evaluation";
        }
        return "redirect:/professors/myCourse";
    }
    //수정성적
    @GetMapping(value = "/professors/updateEvaluation/{courseId}/{studentId}")
    public String update(@PathVariable("courseId") String courseId,@PathVariable("studentId")String studentId, Model model) {

        try {
            Enroll enroll = professorService.findStudentsByCourseIdAndStudentId(courseId,studentId);
            EvaluationFormDto evaluationFormDto = evaluationService.updateEvaluationDtl(studentId,courseId);
            model.addAttribute("enroll",enroll);
            model.addAttribute("evaluationFormDto", evaluationFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "에러가 발생했습니다.");
            //에러발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("evaluationFormDto", new EvaluationFormDto());
            return "professor/updateEvaluation";
        }


        return "professor/updateEvaluation";
    }
    //성적 수정
    @PostMapping(value = "professors/updateEvaluation/{courseId}/{studentId}")
    public String updateEvaluation(EvaluationFormDto evaluationFormDto, RedirectAttributes redirectAttributes){
        try {
            evaluationService.updateEvaluation(evaluationFormDto);
            return "redirect:/professors/myCourse";
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage","오류가 발생했습니다.");
            return "professor/evaluation";
        }
    }
    //출석체크 강의
    @GetMapping(value = {"professors/checkAttendance","/professors/checkAttendance/{page}"})
    public String checkAttendance(@ModelAttribute MyCourseSearchDto myCourseSearchDto,Model model, Principal principal, @PathVariable("page") Optional<Integer> page){
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

        model.addAttribute("maxPage", 5);
        return "professor/checkAttendance";
    }
    //출석체크 리스트
    @GetMapping(value = {"professors/checkAttendanceList/{courseId}","professors/checkAttendanceList/{courseId}/{page}"})
    public String checkAttendanceList(Model model, @PathVariable("courseId") String courseId, @PathVariable("page") Optional<Integer> page){

        int pageSize = 10;
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get():0 , pageSize);

        Course myCourse = courseService.findById(courseId);


        // 페이징된 데이터 가져오기
        Page<AttendanceWeekListDto> attendancePage = professorService.getAttendancePage(myCourse, pageable);


        model.addAttribute("myCourses", attendancePage);
        model.addAttribute("maxPage", 5);
        model.addAttribute("courseId",courseId);


        return "professor/checkAttendanceList";
    }

    @GetMapping(value = {"professors/checkAttendanceStudent/{courseId}","professors/checkAttendanceStudent/{courseId}/{page}"})
    public String checkAttendanceStudent(Model model, @PathVariable("courseId") String courseId,@PathVariable("page") Optional<Integer> page,@RequestParam("week") int week,
                                         @RequestParam("day") String day) {
        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0, 5);
        Page<Enroll> myCourseStudentList = professorService.getMyCourseStudentList(courseId,pageable);
        List<AttendanceStudentListDto> attendanceStudentListDtos = new ArrayList<>();
        for (Enroll enroll : myCourseStudentList) {
            Attendance attendance = attendanceService.findByEnrollIdAndWeekAndDay(enroll.getId(), week, Day.valueOf(day));

            AttendanceStudentListDto attendanceStudentListDto = new AttendanceStudentListDto();
            attendanceStudentListDto.setStudentId(enroll.getStudent().getId());
            attendanceStudentListDto.setStudentName(enroll.getStudent().getName());
            attendanceStudentListDto.setMajorName(enroll.getStudent().getMajor().getTitle());
            if (attendance != null) {
                attendanceStudentListDto.setStatus(attendance.getStatus().toString());
            } else {
                attendanceStudentListDto.setStatus("");
            }
            attendanceStudentListDto.setCourse(enroll.getCourse());
            attendanceStudentListDtos.add(attendanceStudentListDto);
        }
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), attendanceStudentListDtos.size());
        Page<AttendanceStudentListDto> attendancePage = new PageImpl<>(attendanceStudentListDtos.subList(start, end), pageable, attendanceStudentListDtos.size());

        model.addAttribute("attendances",attendancePage);
        model.addAttribute("maxPage", 5);

        return "professor/checkAttendanceStudent";
    }

    @PostMapping(value = "/professors/attendance/add")
    public @ResponseBody ResponseEntity<String> addAttendance(@RequestBody AttendanceFormDto attendanceFormDto,RedirectAttributes redirectAttributes){

        try {
            Attendance attendance = attendanceService.findByCourseIdAndWeekAndDay(attendanceFormDto);
            if(attendance != null){
                attendanceService.updateStatus(attendanceFormDto);
                return new ResponseEntity<String>("출석체크에 성공했습니다.",HttpStatus.OK);
            }
            attendanceService.addAttendance(attendanceFormDto.getStudentId(),attendanceFormDto.getCourseId(),attendanceFormDto.getStatus(),attendanceFormDto.getDay(),attendanceFormDto.getWeek());

            return new ResponseEntity<String>("출석체크에 성공했습니다.",HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<String>("출석체크에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }


}
