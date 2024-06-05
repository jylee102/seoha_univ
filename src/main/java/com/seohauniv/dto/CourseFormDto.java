package com.seohauniv.dto;

import com.seohauniv.entity.Course;
import com.seohauniv.entity.Professor;
import com.seohauniv.entity.Room;
import com.seohauniv.entity.Syllabus;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class CourseFormDto {
    @Pattern(regexp="^\\d{8}-\\d{3}$", message="강의코드 형식이 올바르지 않습니다.")
    private String id;

    private Syllabus syllabus;
    private Professor professor;
    private Room room;

    private static ModelMapper modelMapper = new ModelMapper();

    // entity -> dto
    public static CourseFormDto of(Course course) {
        return modelMapper.map(course, CourseFormDto.class);
    }

    // dto -> entity
    public Course toEntity() {
        return modelMapper.map(this, Course.class);
    }
}
