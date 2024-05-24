package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEnroll is a Querydsl query type for Enroll
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEnroll extends EntityPathBase<Enroll> {

    private static final long serialVersionUID = -903142926L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEnroll enroll = new QEnroll("enroll");

    public final QCourse course;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QStudent student;

    public QEnroll(String variable) {
        this(Enroll.class, forVariable(variable), INITS);
    }

    public QEnroll(Path<? extends Enroll> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEnroll(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEnroll(PathMetadata metadata, PathInits inits) {
        this(Enroll.class, metadata, inits);
    }

    public QEnroll(Class<? extends Enroll> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.course = inits.isInitialized("course") ? new QCourse(forProperty("course"), inits.get("course")) : null;
        this.student = inits.isInitialized("student") ? new QStudent(forProperty("student"), inits.get("student")) : null;
    }

}

