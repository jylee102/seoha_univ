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

    public static final QSyllabus syllabus = new QSyllabus("syllabus");

    public final StringPath courseName = createString("courseName");

    public final ListPath<CourseTime, QCourseTime> courseTimes = this.<CourseTime, QCourseTime>createList("courseTimes", CourseTime.class, QCourseTime.class, PathInits.DIRECT2);

    public final EnumPath<com.seohauniv.constant.CourseType> courseType = createEnum("courseType", com.seohauniv.constant.CourseType.class);

    public final NumberPath<Integer> credit = createNumber("credit", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath objective = createString("objective");

    public final StringPath overview = createString("overview");

    public final EnumPath<com.seohauniv.constant.ProcedureStatus> status = createEnum("status", com.seohauniv.constant.ProcedureStatus.class);

    public final StringPath textbook = createString("textbook");

    public final ListPath<WeeklyPlan, QWeeklyPlan> weeklyPlans = this.<WeeklyPlan, QWeeklyPlan>createList("weeklyPlans", WeeklyPlan.class, QWeeklyPlan.class, PathInits.DIRECT2);

    public QSyllabus(String variable) {
        super(Syllabus.class, forVariable(variable));
    }

    public QSyllabus(Path<? extends Syllabus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSyllabus(PathMetadata metadata) {
        super(Syllabus.class, metadata);
    }

}

