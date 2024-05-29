package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusService {
    private final SyllabusRepository syllabusRepository;

    public Syllabus create(SyllabusFormDto syllabusFormDto) {

        Syllabus syllabus = syllabusFormDto.toEntity();
        syllabus.setStatus(ProcedureStatus.PROCESSING);
        return syllabusRepository.save(syllabus);
    }
}
