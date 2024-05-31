package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.CourseTimeDto;
import com.seohauniv.dto.SyllabusFormDto;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.entity.WeeklyPlan;
import com.seohauniv.repository.SyllabusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusService {
    private final SyllabusRepository syllabusRepository;

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
        return syllabusRepository.getSyllabuses(pageable, searchValue);
    }
}
