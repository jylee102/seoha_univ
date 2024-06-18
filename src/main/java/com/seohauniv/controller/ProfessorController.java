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

    // 내(교수) 강의 페이지
    @GetMapping(value = {"/professors/myCourse", "/professors/myCourse/{page}"})
    public String myCourse(@ModelAttribute MyCourseSearchDto myCourseSearchDto, @PathVariable("page") Optional<Integer> page,
                           Model model, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            int searchYear = myCourseSearchDto.getSearchYear() != 0 ? myCourseSearchDto.getSearchYear() : -1;
            int searchSemester = myCourseSearchDto.getSearchSemester() != 0 ? myCourseSearchDto.getSearchSemester() : -1;

            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

            Page<Course> myCourses;
            if (searchYear == -1 && searchSemester == -1) { // 기본값(사용자의 입력값이 없다면)
                myCourses = courseService.myCourse(principal.getName(), pageable);
            } else {
                myCourses = courseService.myCourseSearch(principal.getName(), myCourseSearchDto, pageable);
            }
            model.addAttribute("myCourses", myCourses);
            model.addAttribute("myCourseSearchDto", myCourseSearchDto);
            model.addAttribute("maxPage", 5);
            return "professor/myCourse";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "내 강의 목록을 불러오는 것에 실패했습니다.");
            return "redirect:/";
        }
    }

    // 내 강의 수강생 목록 페이지
    @GetMapping(value = {"/professors/course/{courseId}", "/professors/course/{courseId}/{page}"})
    public String myCourseStudent(@PathVariable("courseId") String courseId,@PathVariable("page") Optional<Integer> page,
                                  Model model, RedirectAttributes redirectAttributes) {
        try {
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
            Page<Enroll> myCourseStudentList = professorService.getMyCourseStudentList(courseId, pageable);
            List<StudentAttendanceDto> attendanceDtoList = new ArrayList<>();

            for (Enroll enroll : myCourseStudentList) {
                // 출결 정보(성적 계산을 위해)
                int countStatusPresent = attendanceService.countByStatusAndStudentId(AttendStatus.PRESENT,
                        enroll.getId(), enroll.getStudent().getId());
                int countStatusLate = attendanceService.countByStatusAndStudentId(AttendStatus.LATE, enroll.getId(),
                        enroll.getStudent().getId());
                int countStatusAbsent = attendanceService.countByStatusAndStudentId(AttendStatus.ABSENT,
                        enroll.getId(), enroll.getStudent().getId());

                StudentAttendanceDto attendanceDto = new StudentAttendanceDto();

                attendanceDto.setStudent(enroll.getStudent());
                attendanceDto.setCountPresent(countStatusPresent);
                attendanceDto.setCountLate(countStatusLate);
                attendanceDto.setCountAbsent(countStatusAbsent);

                // 이미 입력된 성적 데이터
                Evaluation evaluation = evaluationService.findByEnrollId(enroll.getId());
                if (evaluation != null) {
                    attendanceDto.setEvaluation(evaluation);
                }

                // 강의 정보
                Course course = courseService.findById(courseId);
                attendanceDto.setCourse(course);

                attendanceDtoList.add(attendanceDto);
            }

            // 페이징 처리
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), attendanceDtoList.size());
            Page<StudentAttendanceDto> studentAttendance = new PageImpl<>(attendanceDtoList.subList(start, end),
                    pageable, attendanceDtoList.size());
            model.addAttribute("students", studentAttendance);
            model.addAttribute("maxPage", 5);

            return "professor/myCourseStudent";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "수강생 목록을 불러오는 것에 실패했습니다.");
            return "redirect:/";
        }
    }

    // 성적 기입 페이지
    @GetMapping(value = "/professors/evaluation/{courseId}/{studentId}")
    public String evaluation(@PathVariable("courseId") String courseId, @PathVariable("studentId") String studentId,
                             Model model, RedirectAttributes redirectAttributes){
        try {
            Enroll enroll = professorService.findStudentsByCourseIdAndStudentId(courseId, studentId);
            model.addAttribute("enroll", enroll);
            model.addAttribute("evaluationFormDto", new EvaluationFormDto());
            return "professor/evaluation";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "수강생 정보를 불러오는 것에 실패했습니다.");
            return "/professors/course/" + courseId;
        }
    }

    // 성적 데이터 등록
    @PostMapping(value = "/professors/evaluation/{courseId}/{studentId}")
    public String saveEvaluation(EvaluationFormDto evaluationFormDto, RedirectAttributes redirectAttributes, @PathVariable("courseId") String courseId, @PathVariable("studentId") String studentId){
        try {
            // 해당 학생의 성적 데이터 불러오기
            Evaluation evaluations = evaluationService.findByEnrollStudentId(courseId, studentId);
            if(evaluations != null){ // 기입되어 있는 성적이 있다면
                redirectAttributes.addFlashAttribute("errorMessage","이미 기입된 기록이 있습니다.");
                return "redirect:/professors/myCourse";
            }
            evaluationService.saveEvaluation(evaluationFormDto);
            return "redirect:/professors/myCourse";
        }
        catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage","오류가 발생했습니다.");
            return "professor/evaluation";
        }
    }
    
    // 성적 수정 페이지
    @GetMapping(value = "/professors/updateEvaluation/{courseId}/{studentId}")
    public String update(@PathVariable("courseId") String courseId,@PathVariable("studentId")String studentId, Model model) {

        try {
            Enroll enroll = professorService.findStudentsByCourseIdAndStudentId(courseId,studentId);
            EvaluationFormDto evaluationFormDto = evaluationService.updateEvaluationDtl(studentId,courseId);
            model.addAttribute("enroll",enroll);
            model.addAttribute("evaluationFormDto", evaluationFormDto);

            return "professor/updateEvaluation";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "에러가 발생했습니다.");
            //에러발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("evaluationFormDto", new EvaluationFormDto());
            return "professor/updateEvaluation";
        }
    }
    
    //성적 수정
    @PostMapping(value = "/professors/updateEvaluation/{courseId}/{studentId}")
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
    
    // 출석체크 전 강의 목록 페이지
    @GetMapping(value = {"/professors/checkAttendance", "/professors/checkAttendance/{page}"})
    public String checkAttendance(@ModelAttribute MyCourseSearchDto myCourseSearchDto, @PathVariable("page") Optional<Integer> page,
                                  Model model, Principal principal, RedirectAttributes redirectAttributes){
        try {
            int searchYear = myCourseSearchDto.getSearchYear() != 0 ? myCourseSearchDto.getSearchYear() : -1;
            int searchSemester = myCourseSearchDto.getSearchSemester() != 0 ? myCourseSearchDto.getSearchSemester() :
                    -1;
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

            Page<Course> myCourses;
            if (searchYear == -1 && searchSemester == -1) { // 검색어 없다면
                myCourses = courseService.myCourse(principal.getName(), pageable);
            } else {
                myCourses = courseService.myCourseSearch(principal.getName(), myCourseSearchDto, pageable);
            }

            model.addAttribute("myCourses", myCourses);
            model.addAttribute("maxPage", 5);
            return "professor/checkAttendance";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "내 강의 목록을 불러오는 것에 실패했습니다.");
            return "redirect:/";
        }
    }
    
    // 강의 주차 목록 페이지
    @GetMapping(value = {"/professors/checkAttendanceList/{courseId}", "/professors/checkAttendanceList/{courseId}/{page}"})
    public String checkAttendanceList(@PathVariable("courseId") String courseId, @PathVariable("page") Optional<Integer> page, 
                                      Model model, RedirectAttributes redirectAttributes){

        try {
            int pageSize = 10;
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, pageSize);

            Course myCourse = courseService.findById(courseId);


            // 페이징된 데이터 가져오기
            Page<AttendanceWeekListDto> attendancePage = professorService.getAttendancePage(myCourse, pageable);


            model.addAttribute("myCourses", attendancePage);
            model.addAttribute("maxPage", 5);
            model.addAttribute("courseId", courseId);


            return "professor/checkAttendanceList";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "강의 정보를 불러오는 것에 실패했습니다.");
            return "redirect:/";
        }
    }

    // 수강생 출석체크 페이지
    @GetMapping(value = {"/professors/checkAttendanceStudent/{courseId}", "/professors/checkAttendanceStudent/{courseId}/{page}"})
    public String checkAttendanceStudent(Model model, @PathVariable("courseId") String courseId,@PathVariable("page") Optional<Integer> page,@RequestParam("week") int week,
                                         @RequestParam("day") String day) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        try {
            List<Enroll> myCourseStudentList = professorService.getMyCourseStudentList(courseId);

            // AttendanceStudentListDto에 enroll데이터와 attendance 데이터를 넣어 리스트를 재구성한다.
            List<AttendanceStudentListDto> attendanceStudentListDtos = new ArrayList<>();
            for (Enroll enroll : myCourseStudentList) {
                Attendance attendance = attendanceService.findByEnrollIdAndWeekAndDay(enroll.getId(), week,
                        Day.valueOf(day));

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
            // 페이징
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), attendanceStudentListDtos.size());
            Page<AttendanceStudentListDto> attendancePage = new PageImpl<>(attendanceStudentListDtos.subList(start,
                    end), pageable, attendanceStudentListDtos.size());

            model.addAttribute("attendances", attendancePage);
            model.addAttribute("maxPage", 5);

            return "professor/checkAttendanceStudent";
        } catch (Exception e) {
            model.addAttribute("attendances", new PageImpl<>(new ArrayList<>(), pageable, 0));
            model.addAttribute("maxPage", 5);

            return "professor/checkAttendanceStudent";
        }
    }

    // 출석정보 등록
    @PostMapping(value = "/professors/attendance/add")
    public @ResponseBody ResponseEntity<String> addAttendance(@RequestBody AttendanceFormDto attendanceFormDto){

        try {
            Attendance attendance = attendanceService.findByCourseIdAndWeekAndDay(attendanceFormDto);
            
            if (attendance != null){ // 이미 출석정보가 존재하면 출석정보 수정
                attendanceService.updateStatus(attendanceFormDto);
                return new ResponseEntity<String>("출석체크에 성공했습니다.",HttpStatus.OK);
            }
            
            // 출석정보 없다면 새로 등록
            attendanceService.addAttendance(attendanceFormDto.getStudentId(),attendanceFormDto.getCourseId(),attendanceFormDto.getStatus(),attendanceFormDto.getDay(),attendanceFormDto.getWeek());
            return new ResponseEntity<String>("출석체크에 성공했습니다.",HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<String>("출석체크에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }


}
