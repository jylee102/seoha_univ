package com.seohauniv.controller;


import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Schedule;
import com.seohauniv.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return "redirect:/schedule/list";
    }

    //목록
    @GetMapping(value = "/schedule/list")
    public String scheduleManage (ScheduleFormDto scheduleFormDto, Model model){
        List<Schedule> schedules = scheduleService.getAdminSchedule(scheduleFormDto);


        model.addAttribute("schedules", schedules);
        return "schedule/scheduleList";
    }

    //상세페이지
    @GetMapping(value = "/schedule/detail/{scheduleId}")
    public String scheduleDtl(Model model, @PathVariable("scheduleId") Long scheduleId){
       ScheduleFormDto scheduleFormDto = scheduleService.getScheduleDtl(scheduleId);
        model.addAttribute("schedule",scheduleFormDto);
        return "schedule/scheduleDtl";
    }

    //수정페이지
    @GetMapping(value = "/schedule/rewrite/{scheduleId}")
    public String scheduleUpdatePage(@PathVariable("scheduleId") Long scheduleId, Model model) {

        try {
            ScheduleFormDto scheduleFormDto = scheduleService.updateScheduleDtl(scheduleId);
            model.addAttribute("scheduleFormDto", scheduleFormDto);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "에러가 발생했습니다.");
            //에러발생시 비어있는 객체를 넘겨준다.
            model.addAttribute("scheduleFormDto", new ScheduleFormDto());
            return "schedule/scheduleRewrite";
        }
        return "schedule/scheduleRewrite";
    }

    //수정처리
    @PostMapping(value = "/schedule/rewrite/{scheduleId}")
    public String scheduleUpdate(@Valid ScheduleFormDto scheduleFormDto, BindingResult bindingResult, Model model,
                               @PathVariable("scheduleId") Long scheduleId){
        if (bindingResult.hasErrors()) return "schedule/list";

        ScheduleFormDto getScheduleFormDto = scheduleService.updateScheduleDtl(scheduleId);

        try {
            scheduleService.updateSchedule(scheduleFormDto);
        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "수정 중에 에러가 발생했습니다.");
            model.addAttribute("scheduleFormDto", getScheduleFormDto);
            return "schedule/scheduleDtl";
        }
        return "redirect:/schedule/list";
    }

    //삭제
    @DeleteMapping("/schedule/{scheduleId}/delete")
    public @ResponseBody ResponseEntity deleteSchedule(@PathVariable("scheduleId") Long scheduleId
    ) {

        scheduleService.deleteSchedule(scheduleId);

        return new ResponseEntity<Long>(scheduleId, HttpStatus.OK);
    }

}