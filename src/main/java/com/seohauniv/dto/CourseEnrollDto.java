package com.seohauniv.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.seohauniv.constant.Day;
import com.seohauniv.entity.Room;
import com.seohauniv.entity.Syllabus;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class CourseEnrollDto {
    private String id;

    private Syllabus syllabus;
    private String professorName; // 교수
    private String major;
    private int restSeat;
    private Room room;

    public CourseEnrollDto() {
    }

    public String getCourseTimeDescription() {
        SyllabusFormDto syllabusFormDto = SyllabusFormDto.of(this.syllabus);
        return syllabusFormDto.getCourseTimesDescription().replace("<br>", " / ");
    }

    @QueryProjection
    public CourseEnrollDto(String id, Syllabus syllabus, String professorName, String major, int restSeat, Room room) {
        this.id = id;
        this.professorName = professorName;
        this.syllabus = syllabus;
        this.major = major;
        this.restSeat = restSeat;
        this.room = room;
    }
}
