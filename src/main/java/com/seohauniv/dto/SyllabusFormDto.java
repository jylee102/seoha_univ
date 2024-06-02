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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    // CourseTimes의 설명을 얻는 메소드
    public String getCourseTimesDescription() {
        StringBuilder convertedDays = new StringBuilder();

        Map<String, List<String>> courseMap = new HashMap<>();

        for (CourseTimeDto courseTime : this.courseTimes) {
            String key = courseTime.getCourseTimeDescription();

            if (!courseMap.containsKey(key)) {
                courseMap.put(key, new ArrayList<>());
            }
            courseMap.get(key).add(courseTime.getDay().getDescription());
        }

        for (Map.Entry<String, List<String>> entry : courseMap.entrySet()) {
            for (String str : entry.getValue()) {
                convertedDays.append(str).append("•");
            }
            convertedDays.replace(convertedDays.length() - 1, convertedDays.length(), " ");
            convertedDays.append(entry.getKey()).append("<br>");
        }
        convertedDays.delete(convertedDays.length() - 4, convertedDays.length());

        return convertedDays.toString();
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
