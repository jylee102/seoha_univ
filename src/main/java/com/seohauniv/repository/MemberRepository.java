package com.seohauniv.repository;

import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {

    // select * from member where email = ?
    Member findByEmail(String email); // 회원가입시 중복된 회원이 있는지 이메일로 확인
}
