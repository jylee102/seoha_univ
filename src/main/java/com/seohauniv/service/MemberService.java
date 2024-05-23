package com.seohauniv.service;

import com.seohauniv.constant.Role;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import com.seohauniv.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member createMember(Object entity) {
        Member member = new Member();
        String rawPw = generateRawPassword(entity);

        if (entity instanceof Staff staff) {
            member.setId(staff.getId());
            member.setName(staff.getName());
            member.setEmail(staff.getEmail());
            member.setRole(Role.STAFF);
            staff.setMember(member);
            member.setStaff(staff);
        } else if (entity instanceof Student student) {
            member.setId(student.getId());
            member.setName(student.getName());
            member.setEmail(student.getEmail());
            member.setRole(Role.STUDENT);
            student.setMember(member);
            member.setStudent(student);
        } else if (entity instanceof Professor professor) {
            member.setId(professor.getId());
            member.setName(professor.getName());
            member.setEmail(professor.getEmail());
            member.setRole(Role.PROFESSOR);
            professor.setMember(member);
            member.setProfessor(professor);
        }

        String pw = passwordEncoder.encode(rawPw);
        member.setPassword(pw);

        return memberRepository.save(member);
    }

    private String generateRawPassword(Object entity) {
        LocalDate birth = null;
        if (entity instanceof Staff) {
            birth = ((Staff) entity).getBirth();
        } else if (entity instanceof Student) {
            birth = ((Student) entity).getBirth();
        } else if (entity instanceof Professor) {
            birth = ((Professor) entity).getBirth();
        }

        return "SH" +
                String.valueOf(birth.getYear()).substring(2) +
                String.format("%02d", birth.getMonthValue()) +
                String.format("%02d!", birth.getDayOfMonth());
    }

    public Long count() {
        return memberRepository.count();
    }
}
