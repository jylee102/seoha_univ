package com.seohauniv.service;

import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Professor;
import com.seohauniv.repository.EnrollRepository;
import com.seohauniv.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return enrollRepository.findByCourseId(courseId, pageable);
    }

    @Transactional(readOnly = true)
    public Enroll findStudentsByCourseIdAndStudentId(String courseId,String studentId) {
        return enrollRepository.findByCourseIdAndStudentId(courseId,studentId);
    }
}
