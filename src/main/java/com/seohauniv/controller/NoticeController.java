package com.seohauniv.controller;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

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
        return "redirect:/notice/list";
    }

    //목록
    @GetMapping(value = {"/notice/list", "/notice/list/{page}"})
    public String noticeManage (NoticeSearchDto noticeSearchDto,
                                @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Notice> notices = noticeService.getAdminNoticePage(noticeSearchDto, pageable);
        System.out.println(notices);
        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);

        return "notice/noticeList";
    }


    //상세페이지
    @GetMapping(value = "/notice/detail/{noticeId}")
    public String noticeDtl(Model model, @PathVariable("noticeId") Long noticeId){
        NoticeFormDto noticeFormDto = noticeService.getNoticeDtl(noticeId);
        model.addAttribute("notice",noticeFormDto);
        return "notice/noticeDtl";
    }


    //수정페이지
    @GetMapping(value = "/notice/rewrite/{noticeId}")
    public String noticeUpdatePage(@PathVariable("noticeId") Long noticeId, Model model) {

        try {
            NoticeFormDto noticeFormDto = noticeService.updateNoticeDtl(noticeId);
            model.addAttribute("noticeFormDto", noticeFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "에러가 발생했습니다.");
            //에러발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("noticeFormDto", new NoticeFormDto());
            return "notice/noticeRewrite";
        }


        return "notice/noticeRewrite";
    }

    //수정
    @PostMapping(value = "/notice/rewrite/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model,
                             @PathVariable("noticeId") Long noticeId){
        if (bindingResult.hasErrors()) return "notice/list";

        NoticeFormDto getNoticeFormDto = noticeService.updateNoticeDtl(noticeId);

        try {
            noticeService.updateNotice(noticeFormDto);
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "수정 중에 에러가 발생했습니다.");
            model.addAttribute("noticeFormDto", getNoticeFormDto);
            return "notice/noticeDtl";
        }
        return "redirect:/notice/list";
    }

}
