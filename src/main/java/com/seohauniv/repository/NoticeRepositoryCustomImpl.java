package com.seohauniv.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seohauniv.dto.MainNoticeDto;
import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.QNotice;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression regDtsAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now(); //현재시간

        if(StringUtils.equals("all", searchDateType) || searchDateType == null) {
            return null;
        } else if (StringUtils.equals("1d", searchDateType)) {
            dateTime = dateTime.minusDays(1); //1일전
        } else if (StringUtils.equals("1w", searchDateType)) {
            dateTime = dateTime.minusWeeks(1); //1주일 전
        } else if (StringUtils.equals("1m", searchDateType)) {
            dateTime = dateTime.minusMonths(1); //1개월 전
        } else if (StringUtils.equals("6m", searchDateType)) {
            dateTime = dateTime.minusMonths(6); //6개월 전
        }
        return QNotice.notice.regDate.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {

        if(StringUtils.equals("title", searchBy)) { //상품명으로 검색 시
            return QNotice.notice.title.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)) { //등록자 검색 시
            return QNotice.notice.id.like("%" + searchQuery + "%");
        }

        return null;
    }
    @Override
    public Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        List<Notice> content = queryFactory
                .selectFrom(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count).from(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchByLike(noticeSearchDto.getSearchBy(),noticeSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MainNoticeDto> getMainNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        return null;
    }

    private  BooleanExpression noticeTitleLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery)  ? null : QNotice.notice.title.like("%"+searchQuery+"%");
    }
}
