package com.seohauniv.repository;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.BreakDto;
import com.seohauniv.dto.BreakFormDto;
import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.Break;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BreakRepository extends JpaRepository<Break, Long> {
    List<Break> findByStudentId(String memberId);
    List<Break> findByStudentMemberIdAndStatus(String memberId, ProcedureStatus status);

    @Query("SELECT b " +
            "FROM Break b " +
            "JOIN b.student s " +
            "WHERE b.status = com.seohauniv.constant.ProcedureStatus.PROCESSING " +
            "AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(s.id) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "ORDER BY s.id DESC")
    Page<BreakDto> getBreaks(Pageable pageable, @Param("searchValue") String searchValue);
}
