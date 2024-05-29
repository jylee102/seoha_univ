package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseTime is a Querydsl query type for CourseTime
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseTime extends EntityPathBase<CourseTime> {

    private static final long serialVersionUID = 683912404L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseTime courseTime = new QCourseTime("courseTime");

    public final EnumPath<com.seohauniv.constant.Day> day = createEnum("day", com.seohauniv.constant.Day.class);

    public final TimePath<java.time.LocalTime> endTime = createTime("endTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final TimePath<java.time.LocalTime> startTime = createTime("startTime", java.time.LocalTime.class);

    public final QSyllabus syllabus;

    public QCourseTime(String variable) {
        this(CourseTime.class, forVariable(variable), INITS);
    }

    public QCourseTime(Path<? extends CourseTime> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseTime(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseTime(PathMetadata metadata, PathInits inits) {
        this(CourseTime.class, metadata, inits);
    }

    public QCourseTime(Class<? extends CourseTime> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.syllabus = inits.isInitialized("syllabus") ? new QSyllabus(forProperty("syllabus")) : null;
    }

}

