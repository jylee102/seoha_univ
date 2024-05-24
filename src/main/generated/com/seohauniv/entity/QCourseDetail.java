package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourseDetail is a Querydsl query type for CourseDetail
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourseDetail extends EntityPathBase<CourseDetail> {

    private static final long serialVersionUID = -351728488L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourseDetail courseDetail = new QCourseDetail("courseDetail");

    public final QCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QStudent student;

    public QCourseDetail(String variable) {
        this(CourseDetail.class, forVariable(variable), INITS);
    }

    public QCourseDetail(Path<? extends CourseDetail> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourseDetail(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourseDetail(PathMetadata metadata, PathInits inits) {
        this(CourseDetail.class, metadata, inits);
    }

    public QCourseDetail(Class<? extends CourseDetail> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course"), inits.get("course")) : null;
        this.student = inits.isInitialized("student") ? new QStudent(forProperty("student"), inits.get("student")) : null;
    }

}

