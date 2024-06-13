package com.seohauniv.repository;

import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.Syllabus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SyllabusRepository extends JpaRepository<Syllabus, Long>  {

    @Query("SELECT new com.seohauniv.dto.SyllabusFormDto(s.id, s.courseName, s.courseType, s.credit, s.status, p.name, d.title) " +
            "FROM Syllabus s " +
            "JOIN s.professor p " +
            "JOIN p.major d " +
            "WHERE s.status = com.seohauniv.constant.ProcedureStatus.PROCESSING " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :searchValue, '%')) " +
            "OR LOWER(s.courseName) LIKE LOWER(CONCAT('%', :searchValue, '%'))) " +
            "ORDER BY s.id DESC")
    Page<SyllabusFormDto> getSyllabuses(Pageable pageable, @Param("searchValue") String searchValue);
}
