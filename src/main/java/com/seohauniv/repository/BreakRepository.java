package com.seohauniv.repository;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.entity.Break;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BreakRepository extends JpaRepository<Break, Long> {
    List<Break> findByStudentId(String memberId);
    List<Break> findByStudentMemberIdAndStatus(String memberId, ProcedureStatus status);
}
