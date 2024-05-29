package com.seohauniv.dto;

import com.seohauniv.constant.CourseType;
import com.seohauniv.entity.Syllabus;
import com.seohauniv.entity.WeeklyPlan;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;

@Getter
@Setter
public class SyllabusFormDto {

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

    private List<WeeklyPlan> weeklyPlans;

    @NotEmpty(message = "강의 시간을 추가하세요.")
    @Valid
    private List<CourseTimeDto> courseTimes;

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
