package com.seohauniv.service;

import com.seohauniv.entity.Professor;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfessorService {
    private final ProfessorRepository professorRepository;

    public Professor findById(String id) {
        return professorRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
