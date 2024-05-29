package com.seohauniv.controller;

import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.service.SyllabusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SyllabusController {
    private final SyllabusService syllabusService;

    @PostMapping("/professor/applyForSyllabus")
    public @ResponseBody ResponseEntity applyForSyllabus(@RequestBody @Valid SyllabusFormDto syllabusFormDto, BindingResult bindingResult) {
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
            Syllabus syllabus = syllabusService.create(syllabusFormDto);
            return new ResponseEntity(syllabus, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("강의계획서 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
