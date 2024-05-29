package com.seohauniv.config;

import com.seohauniv.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberContext extends User {

    //Authentication 객체에 저장하고 싶은 값을 필드로 지정
    private final String email;
    private final String name;
    private final String role;

    public MemberContext(Member member, List<GrantedAuthority> authorities) {
        super(member.getId(), member.getPassword(), authorities); //Member 생성자 실행
        this.email = member.getEmail();
        this.name = member.getName();
        this.role = member.getRole().toString();
    }
}