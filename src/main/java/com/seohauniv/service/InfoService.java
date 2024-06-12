package com.seohauniv.service;

import com.seohauniv.constant.Role;
import com.seohauniv.dto.InfoFormDto;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InfoService {
    private final MemberService memberService;

    public Member updateInfo(InfoFormDto infoFormDto, String memberId) {
        Member member = memberService.getMember(memberId);
        if (member.getRole().equals(Role.STAFF)) {
            Staff staff = member.getStaff();
            staff.setPhone(infoFormDto.getPhone());
            staff.setEmail(infoFormDto.getEmail());
            staff.setAddress(infoFormDto.getAddress());

            member.setEmail(staff.getEmail());
            member.setStaff(staff);
        } else if (member.getRole().equals(Role.STUDENT)) {
            Student student = member.getStudent();
            student.setPhone(infoFormDto.getPhone());
            student.setEmail(infoFormDto.getEmail());
            student.setAddress(infoFormDto.getAddress());

            member.setEmail(student.getEmail());
            member.setStudent(student);
        } else if (member.getRole().equals(Role.PROFESSOR)) {
            Professor professor = member.getProfessor();
            professor.setPhone(infoFormDto.getPhone());
            professor.setEmail(infoFormDto.getEmail());
            professor.setAddress(infoFormDto.getAddress());

            member.setEmail(professor.getEmail());
            member.setProfessor(professor);
        }

        return member;
    }

}
