package com.seohauniv.controller;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Schedule;
import com.seohauniv.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;
    //학사일정작성 페이지
    @GetMapping(value = "/staff/schedule/write")
    public String scheduleWrite (Model model){
        model.addAttribute("scheduleFormDto", new ScheduleFormDto());

        return "schedule/scheduleWrite";
    }

    //작성
    @PostMapping(value = "/staff/schedule/write/new")
    public String scheduleWriteNew (@Valid ScheduleFormDto scheduleFormDto, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors())
            return "schedule/scheduleWrite";

        try {
            scheduleService.saveSchedule(scheduleFormDto);
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("errorMessage","오류가 발생했습니다.");
            return "schedule/scheduleWrite";
        }
        return "redirect:/";
    }

    @GetMapping(value = "/schedule/list")
    public String scheduleManage (ScheduleFormDto scheduleFormDto, Model model){
        List<Schedule> schedules = scheduleService.getAdminSchedule(scheduleFormDto);

        model.addAttribute("schedules", schedules);
        return "schedule/scheduleList";
    }
}
