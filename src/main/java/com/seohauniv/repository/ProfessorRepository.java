package com.seohauniv.repository;

import com.seohauniv.entity.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, String> {

    Page<Professor> findById(String searchQuery, Pageable pageable);

    Page<Professor> findByNameContaining(String searchQuery, Pageable pageable);

    Page<Professor> findByIdStartingWith(String searchQuery, Pageable pageable);
}