package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAcademicStat is a Querydsl query type for AcademicStat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAcademicStat extends EntityPathBase<AcademicStat> {

    private static final long serialVersionUID = -414432185L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAcademicStat academicStat = new QAcademicStat("academicStat");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.seohauniv.constant.AcademicStatus> status = createEnum("status", com.seohauniv.constant.AcademicStatus.class);

    public final QStudent student;

    public QAcademicStat(String variable) {
        this(AcademicStat.class, forVariable(variable), INITS);
    }

    public QAcademicStat(Path<? extends AcademicStat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAcademicStat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAcademicStat(PathMetadata metadata, PathInits inits) {
        this(AcademicStat.class, metadata, inits);
    }

    public QAcademicStat(Class<? extends AcademicStat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.student = inits.isInitialized("student") ? new QStudent(forProperty("student"), inits.get("student")) : null;
    }

}

