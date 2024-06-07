package com.seohauniv.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.seohauniv.dto.QCourseEnrollDto is a Querydsl Projection type for CourseEnrollDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCourseEnrollDto extends ConstructorExpression<CourseEnrollDto> {

    private static final long serialVersionUID = -1541160112L;

    public QCourseEnrollDto(com.querydsl.core.types.Expression<String> id, com.querydsl.core.types.Expression<? extends com.seohauniv.entity.Syllabus> syllabus, com.querydsl.core.types.Expression<String> professorName, com.querydsl.core.types.Expression<String> major, com.querydsl.core.types.Expression<Integer> restSeat, com.querydsl.core.types.Expression<? extends com.seohauniv.entity.Room> room) {
        super(CourseEnrollDto.class, new Class<?>[]{String.class, com.seohauniv.entity.Syllabus.class, String.class, String.class, int.class, com.seohauniv.entity.Room.class}, id, syllabus, professorName, major, restSeat, room);
    }

}

