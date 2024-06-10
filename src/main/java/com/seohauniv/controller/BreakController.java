package com.seohauniv.controller;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.BreakFormDto;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Student;
import com.seohauniv.service.BreakService;
import com.seohauniv.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BreakController {
    private final BreakService breakService;
    private final MemberService memberService;
    //휴학작성 페이지
    @GetMapping(value = "/students/applyForLeave")
    public String breakForm (Model model, Principal principal){
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("breakFormDto", new BreakFormDto());


        Student student = member.getStudent();
        model.addAttribute("member",student);
        return "break/breakWrite"; // 해당 경로로 파일을 찾아서 클라이언트에 반환하여 뷰로 보여준다.
    }

    //작성
    @PostMapping(value = "/break/write/new") //
    public String breakInsertNew (@Valid BreakFormDto breakFormDto, BindingResult bindingResult, Model model, Principal principal){
        // @Valid: 유효성 검사를 요청 , BindingResult: 검증 결과를 저장, model: 뷰로 데이터를 전달하는데 사용
        Member member = memberService.getMember(principal.getName());
        Student student = member.getStudent();
        model.addAttribute("member",student);

        //status가 처리중일때 데이터저장 x
        List<Break> processingBreaks = breakService.getProcessingBreaks(principal.getName());
        if (!processingBreaks.isEmpty()) {
            model.addAttribute("errorMessage", "이미 처리중인 휴학 내역이 있습니다.");
            return "break/breakWrite";
        }

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            //유효성 체크후 에러결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()); //에러메세지를 가지고온다.
            }
            model.addAttribute("errorMessage", sb.toString());

            //new ResponseEntity<첫번째 매개변수의 타입>(response 데이터, response status 코드);
            return "break/breakWrite";
        }

        try {
            Break breakEntity = breakFormDto.creatBreak();
            breakEntity.setStatus(ProcedureStatus.PROCESSING);
            breakService.saveBreak(breakFormDto);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","오류가 발생했습니다.");
            return "break/breakWrite";
            //포스트 저장중에 예외가 발생하면 예외를 처리하고 에러 메시지를 설정한 후 다시 해당 경로로 이동.
        }
        return "redirect:/myInfo";
    }

    //삭제
    @DeleteMapping("/break/{breakId}/delete")
    public @ResponseBody ResponseEntity deleteBreak(@PathVariable("breakId") Long breakId) {

        breakService.deleteBreak(breakId);

        return new ResponseEntity<Long>(breakId, HttpStatus.OK);
    }
}
