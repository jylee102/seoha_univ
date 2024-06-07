package com.seohauniv.repository;

import com.seohauniv.entity.Break;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BreakRepository extends JpaRepository<Break, Long>,
        QuerydslPredicateExecutor<Break> {
}
