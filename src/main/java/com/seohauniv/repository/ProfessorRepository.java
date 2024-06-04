package com.seohauniv.repository;

import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

    @Query("SELECT p FROM Professor p WHERE p.id = :searchQuery")
    Page<Professor> getPageById(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT p FROM Professor p WHERE p.name LIKE %:searchQuery%")
    Page<Professor> getPageByName(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT p FROM Professor p WHERE p.id LIKE :searchQuery%")
    Page<Professor> getPageByRegYear(@Param("searchQuery") String searchQuery, Pageable pageable);
}