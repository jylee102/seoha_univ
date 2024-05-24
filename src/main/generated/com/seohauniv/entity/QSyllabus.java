package com.seohauniv.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSyllabus is a Querydsl query type for Syllabus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSyllabus extends EntityPathBase<Syllabus> {

    private static final long serialVersionUID = -709690319L;

    public static final QSyllabus syllabus = new QSyllabus("syllabus");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath objective = createString("objective");

    public final StringPath overview = createString("overview");

    public final StringPath textbook = createString("textbook");

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

