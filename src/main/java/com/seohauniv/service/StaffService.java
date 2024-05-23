package com.seohauniv.service;

import com.seohauniv.constant.Role;
import com.seohauniv.dto.MemberFormDto;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.Staff;
import com.seohauniv.repository.MemberRepository;
import com.seohauniv.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffService {
    private final StaffRepository staffRepository;
    private final MemberService memberService;

    // 교직원 등록
    public Staff saveStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }
}
