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

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusService {
    private final SyllabusRepository syllabusRepository;

    @Transactional(readOnly = true)
    public Syllabus findById(Long id) {
        return syllabusRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // 강의계획서 등록
    public Syllabus create(SyllabusFormDto syllabusFormDto, Professor professor) throws Exception {

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

    // 강의 계획서 리스트(with 페이징 처리)
    @Transactional(readOnly = true)
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

    // 강의 계획서 반려 처리
    public Syllabus refuseSyllabus(Long id) {
        Syllabus syllabus = findById(id);
        syllabus.setStatus(ProcedureStatus.REFUSAL);

        return syllabus;
    }
}
