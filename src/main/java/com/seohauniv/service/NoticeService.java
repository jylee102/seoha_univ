package com.seohauniv.service;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Staff;
import com.seohauniv.repository.NoticeRepository;
import com.seohauniv.repository.StaffRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final StaffRepository staffRepository;
    @PersistenceContext
    private EntityManager entityManager;
    public Long saveNotice(NoticeFormDto noticeFormDto) throws Exception{
        Notice notice = noticeFormDto.creatNotice();
        Staff staff = staffRepository.findById(noticeFormDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        notice.setStaff(staff);
        noticeRepository.save(notice);
        return notice.getId();
    }

    @Transactional(readOnly = true)
    public Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        Page<Notice> noticePage = noticeRepository.getAdminNoticePage(noticeSearchDto, pageable);
        return noticePage;
    }


    //수정 가져오기
    @Transactional(readOnly = true)
    public NoticeFormDto updateNoticeDtl(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
        return noticeFormDto;
    }

    //수정하기
    public Long updateNotice(NoticeFormDto noticeFormDto) throws Exception{
        Notice notice = noticeRepository.findById(noticeFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        notice.updateNotice(noticeFormDto);
        return notice.getId();
    }

    //상세페이지
    public NoticeFormDto getNoticeDtl(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
        notice.updateViews();
        entityManager.flush();
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
        return noticeFormDto;
    }
}
