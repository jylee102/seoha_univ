package com.seohauniv.config;

import com.seohauniv.entity.Staff;
import com.seohauniv.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class MemberInit implements ApplicationRunner {

    private final MemberService memberService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 멤버가 없는 경우에만 초기화 작업 실행
        if (memberService.count() == 0) {
            Staff admin = new Staff();
            admin.setName("이지영");
            admin.setEmail("lilydodo11@gmail.com");
            admin.setBirth(LocalDate.of(2001, 2, 2));
            admin.setPhone("");
            admin.setAddress("");

            memberService.createMember(admin);
        }
    }
}