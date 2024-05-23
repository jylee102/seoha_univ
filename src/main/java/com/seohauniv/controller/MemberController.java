package com.seohauniv.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {
    // 로그인 화면
    @GetMapping(value = "/members/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    // 로그인 실패시
    @GetMapping(value = "/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm"; // 로그인 페이지로 그대로 이동
    }

}
