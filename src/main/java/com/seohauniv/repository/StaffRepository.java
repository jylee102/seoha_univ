package com.seohauniv.repository;

import com.seohauniv.entity.Staff;
import com.seohauniv.entity.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaffRepository extends JpaRepository<Staff, String> {

    @Query("SELECT s FROM Staff s WHERE s.id = :searchQuery")
    Page<Staff> getPageById(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT s FROM Staff s WHERE s.name LIKE %:searchQuery%")
    Page<Staff> getPageByName(@Param("searchQuery") String searchQuery, Pageable pageable);

    @Query("SELECT s FROM Staff s WHERE s.id LIKE :searchQuery%")
    Page<Staff> getPageByRegYear(@Param("searchQuery") String searchQuery, Pageable pageable);
}
