package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCourse is a Querydsl query type for Course
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCourse extends EntityPathBase<Course> {

    private static final long serialVersionUID = -959385241L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCourse course = new QCourse("course");

    public final NumberPath<Integer> capacity = createNumber("capacity", Integer.class);

    public final EnumPath<com.seohauniv.constant.CourseType> courseType = createEnum("courseType", com.seohauniv.constant.CourseType.class);

    public final NumberPath<Integer> credit = createNumber("credit", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final QProfessor professor;

    public final NumberPath<Integer> restSeat = createNumber("restSeat", Integer.class);

    public final QRoom room;

    public final NumberPath<Integer> semester = createNumber("semester", Integer.class);

    public final QSyllabus syllabus;

    public final NumberPath<Integer> year = createNumber("year", Integer.class);

    public QCourse(String variable) {
        this(Course.class, forVariable(variable), INITS);
    }

    public QCourse(Path<? extends Course> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCourse(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCourse(PathMetadata metadata, PathInits inits) {
        this(Course.class, metadata, inits);
    }

    public QCourse(Class<? extends Course> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.professor = inits.isInitialized("professor") ? new QProfessor(forProperty("professor"), inits.get("professor")) : null;
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room")) : null;
        this.syllabus = inits.isInitialized("syllabus") ? new QSyllabus(forProperty("syllabus")) : null;
    }

}

