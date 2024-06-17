package com.seohauniv.config;

import com.seohauniv.entity.Member;
import com.seohauniv.entity.Message;
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
    private final List<Message> messages;

    public MemberContext(Member member, List<GrantedAuthority> authorities, List<Message> unreadMessages) {
        super(member.getId(), member.getPassword(), authorities); //Member 생성자 실행
        this.email = member.getEmail();
        this.name = member.getName();
        this.role = member.getRole().toString();
        this.messages = unreadMessages;
    }

    // 사용자가 주어진 역할(role)을 가지고 있는지 확인하는 메소드
    public boolean hasRole(String role) {
        return getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }
}