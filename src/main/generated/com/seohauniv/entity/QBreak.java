package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBreak is a Querydsl query type for Break
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBreak extends EntityPathBase<Break> {

    private static final long serialVersionUID = 245296691L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBreak break$ = new QBreak("break$");

    public final QBaseTimeEntity _super = new QBaseTimeEntity(this);

    public final NumberPath<Integer> fromSemester = createNumber("fromSemester", Integer.class);

    public final NumberPath<Integer> fromYear = createNumber("fromYear", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.seohauniv.constant.LeaveReason> reasonType = createEnum("reasonType", com.seohauniv.constant.LeaveReason.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDate = _super.regDate;

    public final EnumPath<com.seohauniv.constant.ProcedureStatus> status = createEnum("status", com.seohauniv.constant.ProcedureStatus.class);

    public final QStudent student;

    public final NumberPath<Integer> studentGrade = createNumber("studentGrade", Integer.class);

    public final NumberPath<Integer> toSemester = createNumber("toSemester", Integer.class);

    public final NumberPath<Integer> toYear = createNumber("toYear", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updateDate = _super.updateDate;

    public QBreak(String variable) {
        this(Break.class, forVariable(variable), INITS);
    }

    public QBreak(Path<? extends Break> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBreak(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBreak(PathMetadata metadata, PathInits inits) {
        this(Break.class, metadata, inits);
    }

    public QBreak(Class<? extends Break> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.student = inits.isInitialized("student") ? new QStudent(forProperty("student"), inits.get("student")) : null;
    }

}

