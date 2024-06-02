package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.CourseTimeDto;
import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.entity.WeeklyPlan;
import com.seohauniv.repository.SyllabusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusService {
    private final SyllabusRepository syllabusRepository;

    public Syllabus findById(Long id) {
        return syllabusRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public Syllabus create(SyllabusFormDto syllabusFormDto, Professor professor) {

        Syllabus syllabus = syllabusFormDto.toEntity();
        syllabus.setProfessor(professor);
        syllabus.setStatus(ProcedureStatus.PROCESSING);

        List<CourseTime> courseTimeList = new ArrayList<>();
        for (CourseTimeDto courseTimeDto : syllabusFormDto.getCourseTimes()) {
            CourseTime courseTime = courseTimeDto.toEntity();
            courseTime.setSyllabus(syllabus);
            courseTimeList.add(courseTime);
        }
        syllabus.setCourseTimes(courseTimeList);

        List<WeeklyPlan> weeklyPlanList = new ArrayList<>();
        for (WeeklyPlan weeklyPlan : syllabus.getWeeklyPlans()) {
            weeklyPlan.setSyllabus(syllabus);
            weeklyPlanList.add(weeklyPlan);
        }
        syllabus.setWeeklyPlans(weeklyPlanList);
        
        return syllabusRepository.save(syllabus);
    }

    public Page<SyllabusFormDto> getAllSyllabusToRead(Pageable pageable, String searchValue) {
        Page<SyllabusFormDto> syllabusPage = syllabusRepository.getSyllabuses(pageable, searchValue);
        syllabusPage.forEach(this::setCourseTimes);

        return syllabusPage;
    }

    private void setCourseTimes(SyllabusFormDto syllabusFormDto) {
        Syllabus syllabus = findById(syllabusFormDto.getId());

        List<CourseTimeDto> courseTimeDtos
                = syllabus.getCourseTimes().stream().map(this::convertToDto).toList();

        syllabusFormDto.setCourseTimes(courseTimeDtos);
    }

    private CourseTimeDto convertToDto(CourseTime courseTime) {
        return new CourseTimeDto(courseTime.getDay(), courseTime.getStartTime(), courseTime.getEndTime());
    }
}
