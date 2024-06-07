package com.seohauniv.repository;


import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

}
