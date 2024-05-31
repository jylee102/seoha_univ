package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSyllabus is a Querydsl query type for Syllabus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSyllabus extends EntityPathBase<Syllabus> {

    private static final long serialVersionUID = -709690319L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSyllabus syllabus = new QSyllabus("syllabus");

    public final StringPath courseName = createString("courseName");

    public final ListPath<CourseTime, QCourseTime> courseTimes = this.<CourseTime, QCourseTime>createList("courseTimes", CourseTime.class, QCourseTime.class, PathInits.DIRECT2);

    public final EnumPath<com.seohauniv.constant.CourseType> courseType = createEnum("courseType", com.seohauniv.constant.CourseType.class);

    public final NumberPath<Integer> credit = createNumber("credit", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath objective = createString("objective");

    public final StringPath overview = createString("overview");

    public final QProfessor professor;

    public final EnumPath<com.seohauniv.constant.ProcedureStatus> status = createEnum("status", com.seohauniv.constant.ProcedureStatus.class);

    public final StringPath textbook = createString("textbook");

    public final ListPath<WeeklyPlan, QWeeklyPlan> weeklyPlans = this.<WeeklyPlan, QWeeklyPlan>createList("weeklyPlans", WeeklyPlan.class, QWeeklyPlan.class, PathInits.DIRECT2);

    public QSyllabus(String variable) {
        this(Syllabus.class, forVariable(variable), INITS);
    }

    public QSyllabus(Path<? extends Syllabus> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSyllabus(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSyllabus(PathMetadata metadata, PathInits inits) {
        this(Syllabus.class, metadata, inits);
    }

    public QSyllabus(Class<? extends Syllabus> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.professor = inits.isInitialized("professor") ? new QProfessor(forProperty("professor"), inits.get("professor")) : null;
    }

}

