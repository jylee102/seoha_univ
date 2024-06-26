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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    // 공지사항 작성 페이지
    @GetMapping(value = "/staff/notice/write")
    public String noticeWrite (Model model){
        model.addAttribute("noticeFormDto", new NoticeFormDto());

        return "notice/noticeWrite";
    }

    // 공지사항 등록
    @PostMapping(value = "/staff/notice/write/new")
    public String noticeWriteNew (@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors())
            return "notice/noticeWrite";

        try {
            noticeService.saveNotice(noticeFormDto);
            return "redirect:/notice/list";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","오류가 발생했습니다.");
            return "notice/noticeWrite";
        }
    }

    // 공지사항 목록 페이지
    @GetMapping(value = {"/notice/list", "/notice/list/{page}"})
    public String noticeManage (NoticeSearchDto noticeSearchDto,
                                @PathVariable("page") Optional<Integer> page,
                                Model model, RedirectAttributes redirectAttributes){
        try {
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);


            Page<Notice> notices = noticeService.getAdminNoticePage(noticeSearchDto, pageable);

            model.addAttribute("notices", notices);
            model.addAttribute("noticeSearchDto", noticeSearchDto);
            model.addAttribute("maxPage", 5);

            return "notice/noticeList";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "공지사항 목록 불러오기에 실패했습니다.");
            return "redirect:/";
        }
    }


    // 공지사항 상세 페이지
    @GetMapping(value = "/notice/detail/{noticeId}")
    public String noticeDtl(Model model, @PathVariable("noticeId") Long noticeId, RedirectAttributes redirectAttributes){
        try {
            Notice notice = noticeService.getNoticeDtl(noticeId);
            model.addAttribute("notice", notice);
            return "notice/noticeDtl";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "공지사항 정보 불러오기에 실패했습니다.");
            return "redirect:/notice/list";
        }
    }


    // 공지사항 수정 페이지
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

    // 공지사항 수정
    @PostMapping(value = "/notice/rewrite/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                             @PathVariable("noticeId") Long noticeId){
        if (bindingResult.hasErrors()) return "notice/list";

        try {
            noticeService.updateNotice(noticeFormDto);
            return "redirect:/notice/list";
        }catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "수정 중에 에러가 발생했습니다.");
            return "redirect:/notice/rewrite/{noticeId}";
        }
    }

    // 공지 삭제
    @DeleteMapping("/notice/{noticeId}/delete")
    public @ResponseBody ResponseEntity deleteNotice(@PathVariable("noticeId") Long noticeId) {

        try {
            noticeService.deleteNotice(noticeId);
            return new ResponseEntity<>(noticeId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("학사일정 삭제에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
