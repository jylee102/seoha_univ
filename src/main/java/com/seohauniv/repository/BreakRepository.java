package com.seohauniv.repository;

import com.seohauniv.entity.Break;
import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BreakRepository extends JpaRepository<Break, Long>,
        QuerydslPredicateExecutor<Break> {

//    @Query("SELECT b FROM break b WHERE b.student.id = :memberId")
//    List<Break> getBreakInfo(@Param("memberId") String memberId);
}
