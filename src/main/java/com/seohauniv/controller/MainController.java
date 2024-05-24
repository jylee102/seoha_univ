package com.seohauniv.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

public class MainController {

    @GetMapping(value = "/")
    public String main(HttpServletRequest request, Model model, Principal principal) {
//        // 로그인 되어 있지 않다면 로그인 페이지로
//        Object httpStatus = request.getAttribute("HttpStatus");
//        if (httpStatus != null && (int) httpStatus == HttpServletResponse.SC_UNAUTHORIZED)
//            return "/members/login";

        return "index";
    }
}
