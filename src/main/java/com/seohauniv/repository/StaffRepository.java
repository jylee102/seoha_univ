package com.seohauniv.repository;

import com.seohauniv.entity.Staff;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StaffRepository extends JpaRepository<Staff, String> {

    Page<Staff> findById(String searchQuery, Pageable pageable);

    Page<Staff> findByNameContaining(String searchQuery, Pageable pageable);

    Page<Staff> findByIdStartingWith(String searchQuery, Pageable pageable);
}
