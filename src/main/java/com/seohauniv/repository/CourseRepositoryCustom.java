package com.seohauniv.repository;

import com.seohauniv.dto.CourseEnrollDto;
import com.seohauniv.dto.CourseSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CourseRepositoryCustom {
    Page<CourseEnrollDto> getEnrollListPage(CourseSearchDto courseSearchDto, Pageable pageable);
}
