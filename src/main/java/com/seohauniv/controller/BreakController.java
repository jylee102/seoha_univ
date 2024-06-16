package com.seohauniv.controller;

import com.seohauniv.dto.*;
import com.seohauniv.entity.*;
import com.seohauniv.service.BreakService;
import com.seohauniv.service.MemberService;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class BreakController {
    private final BreakService breakService;
    private final MemberService memberService;
    
    /* 학생의 휴학 신청/취소 */
    
    // 휴학 신청 페이지 이동
    @GetMapping(value = "/students/applyForLeave")
    public String breakForm (Model model, Principal principal){
        Member member = memberService.getMember(principal.getName());
        Student student = member.getStudent();

        model.addAttribute("member", student);
        return "break/breakWrite"; // 해당 경로로 파일을 찾아서 클라이언트에 반환하여 뷰로 보여준다.
    }

    // 휴학 신청
    @PostMapping(value = "/break/write/new")
    public String breakInsertNew (@Valid BreakFormDto breakFormDto, BindingResult bindingResult, // @Valid: 유효성 검사를 요청 , BindingResult: 검증 결과를 저장
                                  RedirectAttributes redirectAttributes, Principal principal) {

        try {
            //status가 처리중일때 데이터저장 x
            List<Break> processingBreaks = breakService.getProcessingBreaks(principal.getName());
            if (!processingBreaks.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "이미 처리중인 휴학 내역이 있습니다.");
                return "redirect:/students/applyForLeave";
            }

            if(bindingResult.hasErrors()) {
                StringBuilder sb = new StringBuilder();

                //유효성 체크후 에러결과를 가져온다.
                List<FieldError> fieldErrors = bindingResult.getFieldErrors();

                for (FieldError fieldError : fieldErrors) {
                    sb.append(fieldError.getDefaultMessage()); //에러메세지를 가지고온다.
                }

                redirectAttributes.addFlashAttribute("errorMessage", sb.toString());
                return "redirect:/students/applyForLeave";
            }

            breakService.saveBreak(breakFormDto);
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage","오류가 발생했습니다.");
            return "redirect:/students/applyForLeave";
            //포스트 저장중에 예외가 발생하면 예외를 처리하고 에러 메시지를 설정한 후 다시 해당 경로로 이동.
        }
        return "redirect:/myInfo";
    }

    //휴학 취소(삭제)
    @DeleteMapping("/break/{breakId}/delete")
    public @ResponseBody ResponseEntity deleteBreak(@PathVariable("breakId") Long breakId) {

        try {
            breakService.deleteBreak(breakId);

            //new ResponseEntity<첫번째 매개변수의 타입>(response 데이터, response status 코드);
            return new ResponseEntity(breakId, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("휴학 취소에 실패했습니다.", HttpStatus.BAD_REQUEST);
        }
    }
    
    /* 교직원의 휴학 처리 */

    // 휴학 신청 목록 페이지 이동
    @GetMapping(value = "/staffs/break/list")
    public String breakList (){
        return "break/breakList";
    }

    // 휴학 신청 목록 불러오기
    @GetMapping("/staffs/loadBreakList")
    public @ResponseBody ResponseEntity loadBreakList(@RequestParam(value = "page", defaultValue = "0") Optional<Integer> page,
                                                       @RequestParam(value = "searchValue", defaultValue = "") String searchValue) {
        try {
            Pageable pageable = PageRequest.of(page.orElse(0), 10);
            Page<BreakDto> breakPage = breakService.getAllBreakToRead(pageable, searchValue);

            return new ResponseEntity(breakPage, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("휴학신청서 목록을 불러오는 것에 실패했습니다.\n관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    // 휴학 신청서 상세페이지
    @GetMapping("/staff/break/{breakId}")
    public String breakDtl(@PathVariable("breakId") Long breakId,
                               Model model, RedirectAttributes redirectAttributes) {
        // model: 뷰로 데이터를 전달하는데 사용

        try {
            Break breaks = breakService.findById(breakId);
            model.addAttribute("breaks",breaks);

            return "break/break";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "해당 휴학 신청의 상세정보를 불러오는 것에 실패했습니다.");
            return "redirect:/staffs/loadBreakList";
        }
    }

    // 휴학 반려 처리
    @PostMapping("/staff/refuse/break")
    public @ResponseBody ResponseEntity refuseBreak(@RequestParam("id") Long id) {
        try {
            breakService.refuseBreak(id);
            return new ResponseEntity("휴학 신청서가 반려 처리되었습니다.",HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("반려 처리에 실패했습니다.",HttpStatus.BAD_REQUEST);
        }
    }

    // 휴학 승인 처리
    @PostMapping("/staff/break/create")
    public String createBreak(@ModelAttribute @Valid Break abreak, BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            //유효성 체크후 에러결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n"); //에러메세지를 가지고온다.
            }

            redirectAttributes.addFlashAttribute("message", sb.toString());
            return "redirect:/staff/break/" + abreak.getId(); // 리다이렉트하여 페이지를 리로드
        }

        try {
            Break breaks = breakService.approvalBreak(abreak); // 휴학신청

            redirectAttributes.addFlashAttribute("message", "휴학신청이 완료되었습니다.");
            return "redirect:/staffs/break/list";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "휴학신청중 문제가 발생했습니다.");
            return "redirect:/staff/break/" + abreak.getId();
        }
    }
}
