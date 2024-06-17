package com.seohauniv.repository;

import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {

    Page<Student> findById(String searchQuery, Pageable pageable);

    Page<Student> findByNameContaining(String searchQuery, Pageable pageable);

    Page<Student> findByIdStartingWith(String searchQuery, Pageable pageable);
}
