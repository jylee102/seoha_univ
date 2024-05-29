package com.seohauniv.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.seohauniv.dto.QMainNoticeDto is a Querydsl Projection type for MainNoticeDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QMainNoticeDto extends ConstructorExpression<MainNoticeDto> {

    private static final long serialVersionUID = 1763299456L;

    public QMainNoticeDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<Integer> view) {
        super(MainNoticeDto.class, new Class<?>[]{long.class, String.class, String.class, int.class}, id, title, content, view);
    }

}

