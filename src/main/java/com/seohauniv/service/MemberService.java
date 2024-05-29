package com.seohauniv.service;

import com.seohauniv.config.MemberContext;
import com.seohauniv.constant.Role;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import com.seohauniv.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
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

    public String generateRawPassword(Object entity) {
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

    // 학번/교번으로 회원 찾기
    public Member getMember(String id) {
        return memberRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // 비밀번호 확인(현재 비밀번호와 같은지)
    public Boolean checkPassword(Member member, String password) {
        return passwordEncoder.matches(password, member.getPassword());
    }

    // 비밀번호 변경
    public void updatePassword(Member member, String rawPassword) {
        String password = passwordEncoder.encode(rawPassword);
        member.setPassword(password);
    }

    public Member updateInfo(MemberFormDto memberFormDto,String memberId){
        Member member = getMember(memberId);
        if (member.getRole().equals(Role.STAFF)) {
            Staff staff = member.getStaff();
            staff.setPhone(memberFormDto.getPhone());
            staff.setEmail(memberFormDto.getEmail());
            staff.setAddress(memberFormDto.getAddress());

            member.setEmail(staff.getEmail());
            member.setStaff(staff);
        } else if (member.getRole().equals(Role.STUDENT)) {
            Student student = member.getStudent();
            student.setPhone(memberFormDto.getPhone());
            student.setEmail(memberFormDto.getEmail());
            student.setAddress(memberFormDto.getAddress());

            member.setEmail(student.getEmail());
            member.setStudent(student);
        } else if (member.getRole().equals(Role.PROFESSOR)) {
            Professor professor = member.getProfessor();
            professor.setPhone(memberFormDto.getPhone());
            professor.setEmail(memberFormDto.getEmail());
            professor.setAddress(memberFormDto.getAddress());

            member.setEmail(professor.getEmail());
            member.setProfessor(professor);
        }

        return member;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //해당 id 계정을 가진 사용자가 있는지 확인
        Member member = memberRepository.getById(id);

        if (member == null) { //사용자가 없다면
            throw new UsernameNotFoundException(id);
        }

        if (member == null) { //사용자가 없다면
            throw new UsernameNotFoundException(id);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new MemberContext(member, authorities); //Member 객체를 상속받은 MemberContext을 넣어주면 스프링이 알아서 처리한다.
    }

    @Transactional(readOnly = true)
    public Page<Member> getMemberListPage(MemberSearchDto memberSearchDto, Pageable pageable) {
        Page<Member> memberPage = memberRepository.getMemberListPage(memberSearchDto, pageable);
        return memberPage;

    }
}
