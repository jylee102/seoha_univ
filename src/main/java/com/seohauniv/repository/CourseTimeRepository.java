package com.seohauniv.repository;

import com.seohauniv.constant.Day;
import com.seohauniv.entity.Course;
import com.seohauniv.entity.CourseTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseTimeRepository extends JpaRepository<CourseTime, Long> {
}
