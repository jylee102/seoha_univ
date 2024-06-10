package com.seohauniv.service;

import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Professor;
import com.seohauniv.repository.EnrollRepository;
import com.seohauniv.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorService {
    private final ProfessorRepository professorRepository;
    private final EnrollRepository enrollRepository;
    @Transactional(readOnly = true)
    public Professor findById(String id) {
        return professorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
    @Transactional(readOnly = true)
    public Page<Enroll> getMyCourseStudentList(String courseId, Pageable pageable) {
        List<Enroll> enrolls = enrollRepository.findStudentsByCourseId(courseId, pageable);

        Long totalCount = enrollRepository.findStudentsByCourseId(courseId);

        return new PageImpl<>(enrolls, pageable, totalCount);
    }
}
