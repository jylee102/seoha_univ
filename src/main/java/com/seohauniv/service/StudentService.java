package com.seohauniv.service;

import com.seohauniv.entity.Student;
import com.seohauniv.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public Student findById(String id) {
        return studentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}
