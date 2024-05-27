package com.seohauniv.controller;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Member;
import com.seohauniv.service.MemberService;
//import com.seohauniv.util.EmailUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
//    private final EmailUtil emailUtil;

    // 로그인 화면
    @GetMapping(value = "/members/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    // 로그인 실패시
    @GetMapping(value = "/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "학번/교번 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm"; // 로그인 페이지로 그대로 이동
    }

    // 비밀번호 변경
    @PostMapping(value = "/members/changePw")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, Principal principal) {

        Member member = memberService.getMember(principal.getName());
        model.addAttribute("memberFormDto", MemberFormDto.of(member));

        // 현재 비밀번호 확인
        if (!memberService.checkPassword(member, currentPassword)) {
            model.addAttribute("message", "현재 비밀번호가 틀립니다.");
            return "member/changePassword";
        }

        if (newPassword.length() < 8 || newPassword.length() > 16) {
            model.addAttribute("message", "비밀번호는 8자 ~ 16자 사이로 입력해주세요.");
            return "member/changePassword";
        }

        // 이전에 사용된 비밀번호와 새로운 비밀번호가 다른지 확인
        if (memberService.checkPassword(member, newPassword)) {
            model.addAttribute("message", "새 비밀번호는 이전에 사용한 비밀번호와 달라야 합니다.");
            return "member/changePassword";
        }

        // 새로운 비밀번호와 확인 비밀번호 일치 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "member/changePassword";
        }

        // 비밀번호 업데이트
        memberService.updatePassword(member, newPassword);

        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");

        return "member/changePassword";
    }

    // 멤버 등록
    @PostMapping(value = "/members/new")
    public @ResponseBody ResponseEntity newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                                                  Model model) {
        System.out.println(memberFormDto);
        return new ResponseEntity<>("구성원 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
    }
}
