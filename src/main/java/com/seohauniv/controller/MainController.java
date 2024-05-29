package com.seohauniv.controller;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Dept;
import com.seohauniv.service.DeptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DeptService deptService;

    @GetMapping(value = "/")
    public String main(HttpServletRequest request, Model model, Principal principal) {
        // 로그인 되어 있지 않다면 로그인 페이지로
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        return "index";
    }

    // 비밀번호 변경 페이지
    @GetMapping(value = "/changePw")
    public String changePw(HttpServletRequest request) {
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        return "member/changePassword";
    }

    // 구성원 등록 페이지
    @GetMapping(value = "/staffs/regMember")
    public String regMember(HttpServletRequest request, Model model) {
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        List<Dept> deptList = deptService.getAllDept();

        model.addAttribute("memberFormDto", new MemberFormDto());
        model.addAttribute("deptList", deptList);

        return "staff/memberForm";
    }

    @GetMapping(value = "/notice")
    public String notice(Model model) {
        return "staff/notice";
    }

    @GetMapping(value = "/noticeForm")
    public String noticeForm(Model model) {
        return "staff/noticeForm";
    }

    @GetMapping(value = "/professors/syllabus")
    public String syllabus(HttpServletRequest request) {
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        return "professor/syllabus";
    }

}
