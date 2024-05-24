package com.seohauniv.controller;

import com.seohauniv.entity.Member;
import com.seohauniv.service.MemberService;
import com.seohauniv.util.EmailUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final EmailUtil emailUtil;

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

}
