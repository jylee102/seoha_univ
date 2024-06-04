package com.seohauniv.controller;

import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.service.ProfessorService;
import com.seohauniv.service.SyllabusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SyllabusController {
    private final SyllabusService syllabusService;
    private final ProfessorService professorService;

    @PostMapping("/professor/applyForSyllabus")
    public @ResponseBody ResponseEntity applyForSyllabus(@RequestBody @Valid SyllabusFormDto syllabusFormDto,
                                                         BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            //유효성 체크후 에러결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n"); //에러메세지를 가지고온다.
            }

            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        try {
            Professor professor = professorService.findById(principal.getName());
            Syllabus syllabus = syllabusService.create(syllabusFormDto, professor);
            return new ResponseEntity(syllabus, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("강의계획서 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/staff/loadCourseList")
    public @ResponseBody ResponseEntity loadCourseList(@RequestParam(value = "page") Optional<Integer> page,
                                                       @RequestParam("searchValue") String searchValue) {
        try {
            Pageable pageable = PageRequest.of(page.orElse(0), 10);
            Page<SyllabusFormDto> syllabusPage = syllabusService.getAllSyllabusToRead(pageable, searchValue);
            return new ResponseEntity(syllabusPage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("강의계획서 목록을 불러오는 것에 실패했습니다.\n관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/staff/viewSyllabus/{syllabusId}")
    public String viewSyllabus(@PathVariable("syllabusId") Long syllabusId,
                               Model model) {
        Syllabus syllabus = syllabusService.findById(syllabusId);
        model.addAttribute("syllabusForm", syllabus);
        return "staff/syllabus";
    }
}
