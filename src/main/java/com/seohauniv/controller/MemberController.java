package com.seohauniv.controller;

import com.seohauniv.constant.Role;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.*;
import com.seohauniv.service.DeptService;
import com.seohauniv.service.MemberService;
import com.seohauniv.util.EmailUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
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
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final DeptService deptService;
    private final EmailUtil emailUtil;

    /* 로그인 관련 */

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

    /* 회원 등록 */
    
    // 구성원 등록 페이지
    @GetMapping(value = "/staffs/regMember")
    public String regMember(Model model) {
        try {
            List<Dept> deptList = deptService.getAllDept();
            model.addAttribute("deptList", deptList);
        } catch (Exception e) {
            model.addAttribute("message", "학과 목록을 불러오는 것에 실패했습니다.\n나중에 시도해주세요.");
        }

        model.addAttribute("memberFormDto", new MemberFormDto());
        return "staff/memberForm";
    }

    // 멤버 등록
    @PostMapping(value = "/members/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();

            // 유효성 체크 후 에러 결과를 가져온다.
            List<FieldError> fieldErrors = bindingResult.getFieldErrors(); // 에러 메시지를 가지고 온다.

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage()).append("\n");
            }

            redirectAttributes.addFlashAttribute("message", sb.toString());
            return "redirect:/staffs/regMember";
        }

        try {
            // 이메일 중복 검사
            if (memberService.existsByEmail(memberFormDto.getEmail())) {
                redirectAttributes.addFlashAttribute("message", "이미 사용중인 이메일입니다.");
                return "redirect:/staffs/regMember";
            }

            // 유효성 검사를 통과했다면 회원가입 진행
            switch (memberFormDto.getRole()) {
                case "STUDENT":
                    Student student = new Student(memberFormDto);
                    memberService.createMember(student);
                    break;
                case "STAFF":
                    Staff staff = new Staff(memberFormDto);
                    memberService.createMember(staff);
                    break;
                case "PROFESSOR":
                    Professor professor = new Professor(memberFormDto);
                    memberService.createMember(professor);
                    break;
            }
            redirectAttributes.addFlashAttribute("message", "구성원 등록에 성공했습니다.");
            return "redirect:/staffs/regMember";
        } catch (IllegalStateException e) { // 회원가입이 이미 되어있다면
            redirectAttributes.addFlashAttribute("message", "구성원 등록에 실패했습니다.");
            return "redirect:/staffs/regMember";
        }
    }
    
    /* 비밀번호 변경 */

    // 비밀번호 변경 페이지
    @GetMapping(value = "/changePw")
    public String changePw() {
        return "member/changePassword";
    }

    // 비밀번호 변경
    @PostMapping(value = "/members/changePw")
    public String changePassword(@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, Principal principal) {

        try {
            Member member = memberService.getMember(principal.getName());

            // 현재 비밀번호 확인
            if (!memberService.checkPassword(member, currentPassword)) {
                model.addAttribute("message", "현재 비밀번호가 틀립니다.");
                return "member/changePassword";
            }

            // 새 비밀 번호의 유효성 검사
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
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "비밀번호 변경 중 오류가 발생했습니다. 다시 시도해주세요.");
        }

        return "member/changePassword";
    }
    
    /* 비밀번호 초기화 */
    
    // 사용자가 입력한 학번/교번을 사용 중인 회원에게 인증코드를 이메일로 전송
    @PostMapping(value = "/members/sendVerificationCode/{id}")
    public @ResponseBody ResponseEntity sendNewPassword(@PathVariable("id") String id, HttpSession session) {
        try {
            Member member = memberService.getMember(id);

            String verificationCode = generateRandomCode();
            session.setAttribute("verificationCode", verificationCode);
            emailUtil.sendVerificationCode(member.getEmail(), verificationCode); // 이메일로 인증코드 전송

            return new ResponseEntity("해당 이메일로 인증코드가 전송되었습니다.", HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            ex.printStackTrace();
            return new ResponseEntity("해당 이메일로 된 계정을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity("현재 비밀번호 초기화를 이용할 수 없습니다. 관리자에게 문의하세요.", HttpStatus.BAD_REQUEST);
        }
    }

    // 랜덤한 6자리 인증 코드 생성
    private String generateRandomCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    // 인증코드 확인 후 일치하면 비밀번호 초기화
    @PostMapping("/members/verifyVerificationCode/{verificationCode}/{id}")
    public @ResponseBody ResponseEntity verifyVerificationCode(@PathVariable("verificationCode") String verificationCode,
                                                         @PathVariable("id") String id, HttpSession session) {

        String sessionCode = (String) session.getAttribute("verificationCode");

        // 세션에 저장되어 있는 인증코드와 사용자가 입력한 코드가 일치하는지 확인
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
    
    /* 구성원 명단 */

    // 구성원 명단 페이지
    @GetMapping(value = {"/staffs/manageMember", "/staffs/manageMember/{page}"})
    public String memberList(MemberSearchDto memberSearchDto,
                             @PathVariable("page") Optional<Integer> page,
                             Model model, RedirectAttributes redirectAttributes) {

        try {
            //PageRequest.of(페이지 번호, 한 페이지당 조회할 데이터 갯수);
            //URL path에 페이지가 있으면 해당 페이지 번호를 조회하고, 페이지 번호가 없다면 0 페이지(첫번째 페이지)를 조회
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 20);

            Page<?> members = memberService.getMemberListPage(memberSearchDto, pageable);

            model.addAttribute("members", members);
            model.addAttribute("memberSearchDto", memberSearchDto);

            //상품 관리 페이지 하단에 보여줄 최대 페이지 번호
            model.addAttribute("maxPage", 10);

            return "staff/memberList";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "구성원 명단 불러오기에 실패했습니다.");
            return "redirect:/";
        }
    }
}
