package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWeeklyPlan is a Querydsl query type for WeeklyPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWeeklyPlan extends EntityPathBase<WeeklyPlan> {

    private static final long serialVersionUID = 2023482006L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWeeklyPlan weeklyPlan = new QWeeklyPlan("weeklyPlan");

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSyllabus syllabus;

    public final NumberPath<Integer> week = createNumber("week", Integer.class);

    public QWeeklyPlan(String variable) {
        this(WeeklyPlan.class, forVariable(variable), INITS);
    }

    public QWeeklyPlan(Path<? extends WeeklyPlan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWeeklyPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWeeklyPlan(PathMetadata metadata, PathInits inits) {
        this(WeeklyPlan.class, metadata, inits);
    }

    public QWeeklyPlan(Class<? extends WeeklyPlan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.syllabus = inits.isInitialized("syllabus") ? new QSyllabus(forProperty("syllabus"), inits.get("syllabus")) : null;
    }

}

