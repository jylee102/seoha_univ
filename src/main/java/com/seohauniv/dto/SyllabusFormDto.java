package com.seohauniv.dto;

import com.seohauniv.constant.CourseType;
import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.entity.CourseTime;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.entity.WeeklyPlan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SyllabusFormDto {
    private Long id;

    @NotBlank(message = "강좌명을 입력하세요.")
    private String courseName;

    @NotNull(message = "강의 구분을 선택하세요.")
    private CourseType courseType;

    @Min(value = 1, message = "이수학점은 1 이상이어야 합니다.")
    @Max(value = 3, message = "이수학점은 3 이하이어야 합니다.")
    private int credit;

    private String overview;
    private String objective;
    private String textbook;

    private ProcedureStatus status;
    private String professorName;
    private String major;

    private List<WeeklyPlan> weeklyPlans;

    @NotEmpty(message = "강의 시간을 추가하세요.")
    @Valid
    private List<CourseTimeDto> courseTimes;

    public SyllabusFormDto() {}

    public SyllabusFormDto(Long id, String courseName, CourseType courseType, int credit, ProcedureStatus status, String professorName, String major) {
        this.id = id;
        this.courseName = courseName;
        this.courseType = courseType;
        this.credit = credit;
        this.status = status;
        this.professorName = professorName;
        this.major = major;
    }

    // CourseType의 설명을 얻는 메소드
    public String getCourseTypeDescription() {
        return this.courseType.getDescription();
    }

    public static List<CourseTimeDto> mapCourseTimeList(List<CourseTime> courseTimeList) {
        List<CourseTimeDto> courseTimeDtoList = new ArrayList<>();
        for (CourseTime courseTime : courseTimeList) {
            CourseTimeDto courseTimeDto = new CourseTimeDto();
            courseTimeDto.setDay(courseTime.getDay());
            courseTimeDto.setStartTime(courseTime.getStartTime());
            courseTimeDto.setEndTime(courseTime.getEndTime());
            courseTimeDtoList.add(courseTimeDto);
        }
        return courseTimeDtoList;
    }

    private static ModelMapper modelMapper = new ModelMapper();

    // dto -> entity
    public Syllabus toEntity() {
        return modelMapper.map(this, Syllabus.class);
    }

    // entity -> dto
    public static SyllabusFormDto of(Syllabus syllabus) {
        return modelMapper.map(syllabus, SyllabusFormDto.class);
    }
}
