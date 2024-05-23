package com.seohauniv.service;

import com.seohauniv.constant.Role;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Staff;
import com.seohauniv.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member saveMember(Member member) {
        String pw = passwordEncoder.encode(member.getPassword());
        member.setPassword(pw);
        return memberRepository.save(member);
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Long count() {
        return memberRepository.count();
    }
}
