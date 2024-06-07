package com.seohauniv.repository;

import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("SELECT s FROM Student s WHERE s.id = :searchQuery")
    Page<Student> getPageById(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.name LIKE %:searchQuery%")
    Page<Student> getPageByName(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT s FROM Student s WHERE s.id LIKE :searchQuery%")
    Page<Student> getPageByRegYear(@Param("searchQuery") String searchQuery, Pageable pageable);
}
