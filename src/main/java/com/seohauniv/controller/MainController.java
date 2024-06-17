package com.seohauniv.controller;

import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Schedule;
import com.seohauniv.service.MemberService;
import com.seohauniv.service.NoticeService;
import com.seohauniv.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MemberService memberService;
    private final NoticeService noticeService;
    private final ScheduleService scheduleService;

    @GetMapping(value = "/")
    public String main(Model model) {
        try {
            Pageable pageable = PageRequest.of(0, 5);
            Page<Notice> notices = noticeService.getAdminNoticePage(new NoticeSearchDto("all", "title", ""), pageable);
            List<Schedule> schedules = scheduleService.getAdminSchedule(new ScheduleFormDto());

            model.addAttribute("notices", notices);
            if (schedules.size() > 5) {
                model.addAttribute("schedules", schedules.subList(0, 5));
            } else {
                model.addAttribute("schedules", schedules);
            }

            return "index";
        } catch (Exception e) {
            model.addAttribute("loginErrorMsg", "현재 서비스를 이용할 수 없습니다.");
            return "member/memberLoginForm";
        }
    }

    // 헤더 데이터 업데이트
    @Transactional(readOnly = true)
    @GetMapping("/header-data")
    public String getHeaderData(Model model, Principal principal) {
        try {
            Member member = memberService.getMemberWithMessages(principal.getName());

            model.addAttribute("messages", memberService.getUnreadMessages(principal.getName()));
            return "fragments/header :: header"; // 헤더 템플릿의 fragment를 반환
        } catch (Exception e) {
            model.addAttribute("loginErrorMsg", "현재 서비스를 이용할 수 없습니다.");
            return "member/memberLoginForm";
        }
    }
}
