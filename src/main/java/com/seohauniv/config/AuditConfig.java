package com.seohauniv.config;

import com.seohauniv.entity.Member;
import com.seohauniv.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // JPA의 auditing 기능을 활성화
public class AuditConfig {
    private final MemberRepository memberRepository;

    public AuditConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Bean
    public AuditorAware<Member> auditorProvider() {
        return new AuditorAwareImpl(memberRepository);
    }
}

