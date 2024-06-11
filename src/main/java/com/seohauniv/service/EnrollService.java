package com.seohauniv.service;

import com.seohauniv.entity.*;
import com.seohauniv.repository.EnrollRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EnrollService {
    private final EnrollRepository enrollRepository;
    private final MemberService memberService;

    // 선택한 강의가 이미 신청한 강의인지 확인
    public boolean checkAlreadyEnrolled(Course course, Student student) {
        List<Enroll> enrolls = enrollRepository.findByStudent(student);
        for (Enroll enroll : enrolls) {
            if (course.equals(enroll.getCourse())) return true;
        }

        return false;
    }

    // 선택한 강의의 강의시간과 이미 신청된 강의의 강의시간과 겹치는지 확인
    public boolean checkTimeConflict(Course course, Student student) {
        List<Enroll> enrolls = enrollRepository.findByStudent(student);

        for (Enroll enroll : enrolls) {
            for (CourseTime courseTime : enroll.getCourse().getSyllabus().getCourseTimes()) { // 이미 신청한 강의의 강의시간
                for (CourseTime time : course.getSyllabus().getCourseTimes()) { // 신청하려는 강의의 강의시간
                    if (time.getDay().equals(courseTime.getDay())) {
                        // 시간 범위가 겹치는지 확인
                        if (!(time.getEndTime().isBefore(courseTime.getStartTime()) || time.getStartTime().isAfter(courseTime.getEndTime()))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public Enroll enroll(Student student, Course course) {
        Enroll enroll = new Enroll();
        enroll.setCourse(course);
        enroll.setStudent(student);

        return enrollRepository.save(enroll);
    }

    // 신청한 강의 목록 가져오기
    @Transactional(readOnly = true)
    public Page<Enroll> getMyEnrollList(String memberId, Pageable pageable) {
        return enrollRepository.findByStudentId(memberId, pageable);
    }

    // 본인 확인(현재 로그인한 학생과 수강 신청한 사용자가 같은지 검사)
    @Transactional(readOnly = true)
    public boolean validateEnroll(Long enrollId, String id) {
        // 로그인한 사용자 찾기
        Member curMember = memberService.getMember(id);;

        // 수강신청내역
        Enroll enroll = enrollRepository.findById(enrollId).orElseThrow(EntityNotFoundException::new);

        Member savedMember = enroll.getStudent().getMember(); //주문한 사용자 찾기

        //로그인한 사용자의 이메일과 주문한 사용자의 이메일이 같은지 비교
        if (!StringUtils.equals(curMember.getId(), savedMember.getId())) {
            return false;
        }

        return true;
    }

    // 수강신청 취소
    public void deleteEnroll(Long enrollId) {
        Enroll enroll = enrollRepository.findById(enrollId).orElseThrow(EntityNotFoundException::new);

        enrollRepository.delete(enroll);
    }

    public boolean isAlreadyEnrolled(Course course, String id) {
        List<Enroll> enrolls = enrollRepository.findByCourseAndStudentId(course, id);
        return !enrolls.isEmpty();
    }
}
