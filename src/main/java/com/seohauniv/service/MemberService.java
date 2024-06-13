package com.seohauniv.service;

import com.seohauniv.config.MemberContext;
import com.seohauniv.constant.Role;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.*;
import com.seohauniv.repository.MemberRepository;
import com.seohauniv.repository.ProfessorRepository;
import com.seohauniv.repository.StaffRepository;
import com.seohauniv.repository.StudentRepository;
import com.seohauniv.util.IdGenerator;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final StaffRepository staffRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

    public Member createMember(Object entity) {
        Member member = new Member();
        String rawPw = generateRawPassword(entity);
        LocalDateTime now = LocalDateTime.now();

        if (entity instanceof Staff staff) {
            member.setId(staff.getId());
            member.setName(staff.getName());
            member.setEmail(staff.getEmail());
            member.setRole(Role.STAFF);
            
            // 초기 멤버 등록시 아이디 고정
            if (staff.getEmail().equals("lilydodo11@gmail.com"))
                member.setId("000000");
            else
                member.setId(generateUniqueStaffId(now));
            
            staff.setMember(member);
            member.setStaff(staff);
        } else if (entity instanceof Student student) {
            member.setId(student.getId());
            member.setName(student.getName());
            member.setEmail(student.getEmail());
            member.setRole(Role.STUDENT);
            member.setId(generateUniqueStudentId(now));
            student.setMember(member);
            student.setGrade(1);
            student.setSemester(1);
            member.setStudent(student);
        } else if (entity instanceof Professor professor) {
            member.setId(professor.getId());
            member.setName(professor.getName());
            member.setEmail(professor.getEmail());
            member.setRole(Role.PROFESSOR);
            member.setId(generateUniqueProfessorId(now));
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

    public boolean existsById(String id) {
        return memberRepository.existsById(id);
    }

    public boolean existsByEmail(String email) {
        return memberRepository.findByEmail(email) != null;
    }

    public String generateUniqueStudentId(LocalDateTime regDate) {
        String studentId;
        do {
            studentId = IdGenerator.generateStudentId(regDate);
        } while (existsById(studentId));
        return studentId;
    }

    public String generateUniqueStaffId(LocalDateTime regDate) {
        String staffId;
        do {
            staffId = IdGenerator.generateStaffId(regDate);
        } while (existsById(staffId));
        return staffId;
    }

    public String generateUniqueProfessorId(LocalDateTime regDate) {
        String professorId;
        do {
            professorId = IdGenerator.generateProfessorId(regDate);
        } while (existsById(professorId));
        return professorId;
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

    public Member updateInfo(MemberFormDto memberFormDto, String memberId) {
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

    @Transactional(readOnly = true)
    public Page<?> getMemberListPage(MemberSearchDto memberSearchDto, Pageable pageable) {
        switch (memberSearchDto.getTabValue()) {
            case "STUDENT":
                if (memberSearchDto.getSearchBy().equals("id")) {
                    if (memberSearchDto.getSearchQuery().length() == 2 || memberSearchDto.getSearchQuery().isEmpty()) {
                        return studentRepository.findByIdStartingWith(memberSearchDto.getSearchQuery(), pageable);
                    } else {
                        return studentRepository.findById(memberSearchDto.getSearchQuery(),
                                pageable);
                    }
                } else if (memberSearchDto.getSearchBy().equals("name")) {
                    return studentRepository.findByNameContaining(memberSearchDto.getSearchQuery(),
                            pageable);
                }
            case "STAFF":
                if (memberSearchDto.getSearchBy().equals("id")) {
                    if (memberSearchDto.getSearchQuery().length() == 2 || memberSearchDto.getSearchQuery().isEmpty()) {
                        return staffRepository.findByIdStartingWith(memberSearchDto.getSearchQuery(),
                                pageable);
                    } else {
                        return staffRepository.findById(memberSearchDto.getSearchQuery(),
                                pageable);
                    }
                } else if (memberSearchDto.getSearchBy().equals("name")) {
                    return staffRepository.findByNameContaining(memberSearchDto.getSearchQuery(), pageable);
                }
            case "PROFESSOR":
                if (memberSearchDto.getSearchBy().equals("id")) {
                    if (memberSearchDto.getSearchQuery().length() == 2 || memberSearchDto.getSearchQuery().isEmpty()) {
                        return professorRepository.findByIdStartingWith(memberSearchDto.getSearchQuery(), pageable);
                    } else {
                        return professorRepository.findById(memberSearchDto.getSearchQuery()
                                , pageable);
                    }
                } else if (memberSearchDto.getSearchBy().equals("name")) {
                    return professorRepository.findByNameContaining(memberSearchDto.getSearchQuery(),
                            pageable);
                }
        }
        return null;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true)
    public Member getMemberWithMessages(String id) {
        return entityManager.createQuery(
                        "SELECT m FROM Member m LEFT JOIN FETCH m.messages WHERE m.id = :id", Member.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Transactional(readOnly = true)
    public List<Message> getUnreadMessages(String memberId) {
        return entityManager.createQuery(
                        "SELECT msg FROM Message msg WHERE msg.sendTo.id = :memberId AND msg.isRead = 'f' ORDER BY msg.regDate DESC", Message.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        //해당 id 계정을 가진 사용자가 있는지 확인
        Member member = getMemberWithMessages(id);

        if (member == null) { //사용자가 없다면
            throw new UsernameNotFoundException(id);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + member.getRole().toString()));

        List<Message> unreadMessages = getUnreadMessages(id);

        return new MemberContext(member, authorities, unreadMessages); //Member 객체를 상속받은 MemberContext을 넣어주면 스프링이 알아서 처리한다.
    }
}