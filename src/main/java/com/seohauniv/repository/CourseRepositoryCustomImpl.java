package com.seohauniv.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seohauniv.dto.CourseEnrollDto;
import com.seohauniv.dto.CourseSearchDto;
import com.seohauniv.dto.QCourseEnrollDto;
import com.seohauniv.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.dsl.Expressions;

import java.util.List;

public class CourseRepositoryCustomImpl implements CourseRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CourseEnrollDto> getEnrollListPage(CourseSearchDto courseSearchDto, Pageable pageable) {

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QCourse qCourse = QCourse.course;
        QSyllabus qSyllabus = QSyllabus.syllabus;
        QProfessor qProfessor = QProfessor.professor;
        QDept qDept = QDept.dept;
        QRoom qRoom = QRoom.room;

        BooleanBuilder predicate = new BooleanBuilder();

        // 검색 조건 추가
        if (courseSearchDto.getSearchCourseType() != null) {
            predicate.and(qSyllabus.courseType.eq(courseSearchDto.getSearchCourseType()));
        }

        if (courseSearchDto.getSearchQuery() != null && !courseSearchDto.getSearchQuery().isEmpty()) {
            String searchQuery = "%" + courseSearchDto.getSearchQuery().toLowerCase() + "%";
            predicate.andAnyOf(
                    qProfessor.name.toLowerCase().like(searchQuery),
                    qSyllabus.courseName.toLowerCase().like(searchQuery)
            );
        }

        List<CourseEnrollDto> resultList = queryFactory
                .select(new QCourseEnrollDto(
                        qCourse.id,
                        qSyllabus,
                        qProfessor.name,
                        qDept.title,
                        qCourse.restSeat,
                        qRoom
                ))
                .from(qCourse)
                .join(qCourse.syllabus, qSyllabus)
                .join(qSyllabus.professor, qProfessor)
                .join(qProfessor.major, qDept)
                .join(qCourse.room, qRoom)
                .where(predicate)
                .orderBy(qSyllabus.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(resultList, pageable, totalCount(predicate));
    }

    // 전체 결과 수 계산
    private long totalCount(BooleanBuilder predicate) {
        return new JPAQueryFactory(entityManager)
                .select(Expressions.ONE)
                .from(QCourse.course)
                .where(predicate)
                .fetchCount();
    }
}
