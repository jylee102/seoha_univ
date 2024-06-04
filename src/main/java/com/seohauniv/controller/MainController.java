package com.seohauniv.controller;

import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.Dept;
import com.seohauniv.entity.Member;
import com.seohauniv.service.DeptService;
import com.seohauniv.service.MemberService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final DeptService deptService;
    private final MemberService memberService;

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

    //    @GetMapping(value = "/staffs/manageMember")
    //    public String memberList(Model model) {
    //        return "staff/memberList";
    //    }

    // 구성원 명단 페이지
    @GetMapping(value = {"/staffs/manageMember", "/staffs/manageMember/{page}"})
    public String memberList(MemberSearchDto memberSearchDto,
                             @PathVariable("page") Optional<Integer> page, Model model) {

        //PageRequest.of(페이지 번호, 한 페이지당 조회할 데이터 갯수);
        //URL path에 페이지가 있으면 해당 페이지 번호를 조회하고, 페이지 번호가 없다면 0 페이지(첫번째 페이지)를 조회
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<?> members = memberService.getMemberListPage(memberSearchDto, pageable);

        model.addAttribute("members", members);
        model.addAttribute("memberSearchDto", memberSearchDto);

        //상품 관리 페이지 하단에 보여줄 최대 페이지 번호
        model.addAttribute("maxPage", 10);

        return "staff/memberList";
    }

    @GetMapping(value = "/professors/syllabus")
    public String syllabus(HttpServletRequest request) {
        Object httpStatus = request.getAttribute("HttpStatus");
        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
            return "/members/login";

        return "professor/syllabus";
    }

}
