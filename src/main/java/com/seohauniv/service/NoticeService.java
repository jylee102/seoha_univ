package com.seohauniv.service;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.repository.NoticeRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public Long saveNotice(NoticeFormDto noticeFormDto) {
        Notice notice = noticeFormDto.creatNotice();
        noticeRepository.save(notice);
        return notice.getId();
    }

    @Transactional(readOnly = true)
    public Page<Notice> getAdminNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable) {
        return noticeRepository.getAdminNoticePage(noticeSearchDto, pageable);
    }


    //수정 가져오기
    @Transactional(readOnly = true)
    public NoticeFormDto updateNoticeDtl(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
        return NoticeFormDto.of(notice);
    }

    //수정하기
    public Long updateNotice(NoticeFormDto noticeFormDto) throws Exception{
        Notice notice = noticeRepository.findById(noticeFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        notice.updateNotice(noticeFormDto);
        return notice.getId();
    }

    //상세페이지
    public Notice getNoticeDtl(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
        notice.updateViews();

        return notice;
    }

    //공지 삭제
    public void deleteNotice(Long noticeId) {
        //★delete하기 전에 select 를 한번 해준다
        //->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(EntityNotFoundException::new);

        //delete
        //CasCade 설정을 통해 order의 자식 엔티티에 해당하는 orderItem 도 같이 삭제된다.
        noticeRepository.delete(notice);
    }
}
