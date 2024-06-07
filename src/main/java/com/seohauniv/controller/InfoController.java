package com.seohauniv.controller;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Student;
import com.seohauniv.service.BreakService;
import com.seohauniv.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class InfoController {
    private final MemberService memberService;
    private final BreakService breakService;

    //내정보 페이지 보기
    @GetMapping(value = "/myInfo")
    public String InfoForm(Model model, Principal principal) {

        Member member = memberService.getMember(principal.getName());
        List<Break> breaks = breakService.getBreakInfo(principal.getName());
        switch (member.getRole().toString()) {
            case "STAFF":
                model.addAttribute("member", member.getStaff());
                break;
            case "STUDENT":
                model.addAttribute("member", member.getStudent());
                model.addAttribute("breaks",breaks);
                break;
            case "PROFESSOR":
                model.addAttribute("member", member.getProfessor());
                break;
        }
        return "member/myInfo";
    }

    //내정보 수정페이지
    @GetMapping(value = "/myInfo/update")
    public String InfoDtl (Principal principal, Model model){
       Member member = memberService.getMember(principal.getName());
       MemberFormDto memberFormDto = MemberFormDto.of(member);

       switch (member.getRole().toString()) {
            case "STAFF":
                memberFormDto.setPhone(member.getStaff().getPhone());
                memberFormDto.setAddress(member.getStaff().getAddress());
                memberFormDto.setEmail(member.getStaff().getEmail());
                memberFormDto.setName(member.getStaff().getName());
                memberFormDto.setBirth(member.getStaff().getBirth());
                break;
            case "STUDENT":
                memberFormDto.setPhone(member.getStudent().getPhone());
                memberFormDto.setAddress(member.getStudent().getAddress());
                memberFormDto.setEmail(member.getStudent().getEmail());
                memberFormDto.setName(member.getStudent().getName());
                memberFormDto.setBirth(member.getStudent().getBirth());
                break;
            case "PROFESSOR":
                memberFormDto.setPhone(member.getProfessor().getPhone());
                memberFormDto.setAddress(member.getProfessor().getAddress());
                memberFormDto.setEmail(member.getProfessor().getEmail());
                memberFormDto.setName(member.getProfessor().getName());
                memberFormDto.setBirth(member.getProfessor().getBirth());
                break;
       }
       model.addAttribute("activePage", "myInfo");
       model.addAttribute("memberFormDto",memberFormDto);
       return "member/myInfoUpdate";
    }

    //수정
    @PostMapping(value = "/myInfo/update")
    public String InfoUpdate(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                             Principal principal, Model model){

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            //유효성 체크후 에러결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n"); //에러메세지를 가지고온다.
            }
            model.addAttribute("message", sb.toString());
            model.addAttribute("memberFormDto",memberFormDto);

            return "member/myInfoUpdate";
        }

        try {
            Member member = memberService.updateInfo(memberFormDto, principal.getName());
            switch (member.getRole().toString()) {
                case "STAFF":
                    model.addAttribute("member", member.getStaff());
                    break;
                case "STUDENT":
                    model.addAttribute("member", member.getStudent());
                    break;
                case "PROFESSOR":
                    model.addAttribute("member", member.getProfessor());
                    break;
            }

            return "member/myInfo";
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("memberFormDto",memberFormDto);
            model.addAttribute("message", "수정 중 에러가 발생했습니다");

            return "member/myInfoUpdate";
        }
    }
}
