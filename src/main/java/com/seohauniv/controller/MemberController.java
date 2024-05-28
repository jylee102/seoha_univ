package com.seohauniv.controller;

import com.seohauniv.constant.Role;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Member;
import com.seohauniv.service.MemberService;
import com.seohauniv.util.EmailUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;
    private final EmailUtil emailUtil;

    // 로그인 화면
    @GetMapping(value = "/members/login")
    public String loginMember() {
        return "member/memberLoginForm";
    }

    // 로그인 실패시
    @GetMapping(value = "/members/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "학번/교번 또는 비밀번호를 확인해주세요.");
        return "member/memberLoginForm"; // 로그인 페이지로 그대로 이동
    }

    // 비밀번호 변경
    @PostMapping(value = "/members/changePw")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, Principal principal) {

        Member member = memberService.getMember(principal.getName());
        model.addAttribute("memberFormDto", MemberFormDto.of(member));

        // 현재 비밀번호 확인
        if (!memberService.checkPassword(member, currentPassword)) {
            model.addAttribute("message", "현재 비밀번호가 틀립니다.");
            return "member/changePassword";
        }

        if (newPassword.length() < 8 || newPassword.length() > 16) {
            model.addAttribute("message", "비밀번호는 8자 ~ 16자 사이로 입력해주세요.");
            return "member/changePassword";
        }

        // 이전에 사용된 비밀번호와 새로운 비밀번호가 다른지 확인
        if (memberService.checkPassword(member, newPassword)) {
            model.addAttribute("message", "새 비밀번호는 이전에 사용한 비밀번호와 달라야 합니다.");
            return "member/changePassword";
        }

        // 새로운 비밀번호와 확인 비밀번호 일치 확인
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("message", "새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
            return "member/changePassword";
        }

        // 비밀번호 업데이트
        memberService.updatePassword(member, newPassword);

        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");

        return "member/changePassword";
    }

    // 멤버 등록
    @PostMapping(value = "/members/new")
    public @ResponseBody ResponseEntity newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                                                  Model model) {
        System.out.println(memberFormDto);
        return new ResponseEntity<>("구성원 등록에 실패했습니다.", HttpStatus.BAD_REQUEST);
    }
    
    // 비밀번호 초기화
    @PostMapping(value = "/members/sendVerificationCode/{id}")
    public @ResponseBody ResponseEntity sendNewPassword(@PathVariable("id") String id, HttpSession session) {
        try {
            Member member = memberService.getMember(id);

            if (member == null) {
                return new ResponseEntity("해당 이메일로 된 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
            }

            String verificationCode = generateRandomCode();
            session.setAttribute("verificationCode", verificationCode);
            emailUtil.sendVerificationCode(member.getEmail(), verificationCode); // 이메일로 인증코드 전송

            return new ResponseEntity("해당 이메일로 인증코드가 전송되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("현재 비밀번호 초기화를 이용할 수 없습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    private String generateRandomCode() {
        // 랜덤한 6자리 인증 코드 생성 로직
        return String.format("%06d", new Random().nextInt(1000000));
    }








    @PostMapping("/members/verifyVerificationCode/{verificationCode}/{id}")
    public @ResponseBody ResponseEntity verifyVerificationCode(@PathVariable("verificationCode") String verificationCode,
                                                         @PathVariable("id") String id, HttpSession session) {

        String sessionCode = (String) session.getAttribute("verificationCode");

        if (sessionCode != null && sessionCode.equals(verificationCode)) {
            try {
                Member member = memberService.getMember(id);
                String password = "";
                if (member.getRole() == Role.STAFF)
                    password = memberService.generateRawPassword(member.getStaff());
                else if (member.getRole() == Role.PROFESSOR)
                    password = memberService.generateRawPassword(member.getProfessor());
                else if (member.getRole() == Role.STUDENT)
                    password = memberService.generateRawPassword(member.getStudent());

                memberService.updatePassword(member, password);
                session.removeAttribute("verificationCode"); // 인증 코드를 세션에서 제거
                return new ResponseEntity<>("비밀번호가 성공적으로 초기화 되었습니다.", HttpStatus.OK);

            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>("비밀번호 초기화에 실패했습니다.", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("인증번호가 올바르지 않습니다", HttpStatus.BAD_REQUEST);
        }
    }


//    @GetMapping(value = "/initPw/{id}")
//    public ResponseEntity<String> initPw(@PathVariable("id") String id) {
//        try {
//            Member member = memberService.getMember(id);
//            String password = memberService.generateRawPassword(member);
//
//            memberService.updatePassword(member, password);
//            return new ResponseEntity<String>("비밀번호가 성공적으로 초기화 되었습니다.", HttpStatus.OK);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>("비밀번호 초기화에 실패했습니다.", HttpStatus.BAD_REQUEST);
//        }
//    }
}
