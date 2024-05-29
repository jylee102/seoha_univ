package com.seohauniv.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.seohauniv.dto.MemberSearchDto;
import com.seohauniv.entity.Member;
import com.seohauniv.entity.QMember;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private JPAQueryFactory queryFactory;

    // 생성자 방식으로 의존성 주입
    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("name", searchBy)) { // 이름으로 검색시
            return QMember.member.name.like("%" + searchQuery + "%"); // name like %검색어%
        } else if (StringUtils.equals("id", searchBy)) { // 학번/교번으로 검색시
            return QMember.member.id.like("%" + searchQuery + "%"); // id like %검색어%
        }

        return null; // 쿼리문을 실행하지 않는다.
    }

    @Override
    public Page<Member> getMemberListPage(MemberSearchDto memberSearchDto, Pageable pageable) {
        /*
		  select * from member
		  where id(name) like %검색어% // 학번/교번 or 이름으로 검색 가능
		  order by id asc;
		*/
        List<Member> content = queryFactory
                .selectFrom(QMember.member)
                .where(searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
                .orderBy(QMember.member.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        /*
          select count(*) from member
          where id(name) like %검색어%
        */
        long total = queryFactory.select(Wildcard.count).from(QMember.member)
                .where(searchByLike(memberSearchDto.getSearchBy(), memberSearchDto.getSearchQuery()))
                .fetchOne();

        //pageable 객체 : 한 페이지의 몇개의 게시물을 보여줄지, 시작 페이지 번호에 대한 정보를 가지고 있다.
        return new PageImpl<>(content, pageable, total);
    }
}
