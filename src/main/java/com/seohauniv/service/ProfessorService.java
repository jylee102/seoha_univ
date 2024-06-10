package com.seohauniv.service;

import com.seohauniv.entity.Enroll;
import com.seohauniv.entity.Professor;
import com.seohauniv.repository.EnrollRepository;
import com.seohauniv.repository.ProfessorRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
    public List<Enroll> getMyCourseStudent(String courseId){
        return enrollRepository.findStudentsByCourseId(courseId);
    }
}
