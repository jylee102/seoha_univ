package com.seohauniv.config;

import com.seohauniv.entity.Member;
import com.seohauniv.repository.MemberRepository;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

// 로그인한 사용자의 정보를 등록자와 수정자로 지정하기 위해 사용
public class AuditorAwareImpl implements AuditorAware<Member> {
    private final MemberRepository memberRepository;

    public AuditorAwareImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Optional<Member> getCurrentAuditor() {
        String userId = getCurrentUsernameFromSecurityContext();
        return memberRepository.findById(userId);
    }

    // 로그인한 사용자의 id
    private String getCurrentUsernameFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";

        if (authentication != null) {
            userId = authentication.getName(); // 로그인한 사용자의 id(학번 또는 교번)을 가져옴
        }

        return userId;
    }
}
