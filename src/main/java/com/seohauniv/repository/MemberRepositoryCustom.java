package com.seohauniv.repository;

import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> getMemberListPage(MemberSearchDto memberSearchDto, Pageable pageable);
}
