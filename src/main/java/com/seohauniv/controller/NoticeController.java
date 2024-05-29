package com.seohauniv.controller;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    //작성 페이지
    @GetMapping(value = "/notice/write")
    public String noticeWrite (Model model){
        model.addAttribute("noticeFormDto", new NoticeFormDto());

        return "notice/noticeWrite";
    }

    //작성
    @PostMapping(value = "/notice/write/new")
    public String noticeWriteNew (@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors())
            return "notice/noticeWrite";

        try {
            noticeService.saveNotice(noticeFormDto);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","오류가 발생했습니다.");
            return "notice/noticeWrite";
        }
        return "redirect:/";
    }
}
