package com.seohauniv.controller;

import com.seohauniv.dto.BreakFormDto;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Student;
import com.seohauniv.service.BreakService;
import com.seohauniv.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class BreakController {
    private final BreakService breakService;
    private final MemberService memberService;
    //휴학작성 페이지
    @GetMapping(value = "/students/applyForLeave")
    public String breakForm (Model model, Principal principal){
        Member member = memberService.getMember(principal.getName());
        model.addAttribute("breakFormDto", new BreakFormDto()); //postFormDto 라는 이름의 속성을 모델에 추가함 , 값으로 새로운 postFormDto 객체를 할당한다
        //이렇게 함으로 뷰에서 이 객체에 접근하여 화면에 데이터를 표시할수 있다.

        Student student = member.getStudent();
        model.addAttribute("member",student);
        return "break/breakWrite"; // 해당 경로로 파일을 찾아서 클라이언트에 반환하여 뷰로 보여준다.
    }

    //작성
    @PostMapping(value = "/break/write/new") // 어떠한 기능을 동작했을때 사용되도록 하는 역할
    public String breakInsertNew (@Valid BreakFormDto breakFormDto, BindingResult bindingResult, Model model, Principal principal){
        // @Valid: 유효성 검사를 요청 , BindingResult: 검증 결과를 저장, model: 뷰로 데이터를 전달하는데 사용
        Member member = memberService.getMember(principal.getName());
        Student student = member.getStudent();
        model.addAttribute("member",student);
        if (bindingResult.hasErrors()) return "break/breakWrite"; //유효성검사 오류가 나면 해당 경로로 리턴

        try {
            breakService.saveBreak(breakFormDto);
            //postService 를 사용하고 postFormDto 를 이용하여 포스트를 저장
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","오류가 발생했습니다.");
            return "break/breakWrite";
            //포스트 저장중에 예외가 발생하면 예외를 처리하고 에러 메시지를 설정한 후 다시 해당 경로로 이동.
        }
        return "redirect:/myInfo"; // 포스트작성이 성공적으로 처리되었을때 홈페이지로 이동
    }
}
