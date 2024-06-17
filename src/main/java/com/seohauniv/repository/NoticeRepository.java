package com.seohauniv.repository;

import com.seohauniv.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NoticeRepository extends JpaRepository<Notice, Long>,
        QuerydslPredicateExecutor<Notice>,NoticeRepositoryCustom {

}
