package com.seohauniv.repository;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.entity.Break;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BreakRepository extends JpaRepository<Break, Long> {
    @Query("SELECT b FROM Break b WHERE b.student.id = :memberId")
    List<Break> getBreakInfo(@Param("memberId") String memberId);
    List<Break> findByStudentMemberIdAndStatus(String memberId, ProcedureStatus status);
}
