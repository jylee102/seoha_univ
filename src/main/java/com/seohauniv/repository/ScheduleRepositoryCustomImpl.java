package com.seohauniv.repository;

import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.QNotice;
import com.seohauniv.entity.QSchedule;
import com.seohauniv.entity.Schedule;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public ScheduleRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public List<Schedule> getAdminSchedule(ScheduleFormDto scheduleFormDto) {
        List<Schedule> content = queryFactory
                .selectFrom(QSchedule.schedule)
                .orderBy(QSchedule.schedule.start.asc())
                .fetch();

        return content;
    }
}
