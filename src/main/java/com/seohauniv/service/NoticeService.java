package com.seohauniv.service;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Staff;
import com.seohauniv.repository.NoticeRepository;
import com.seohauniv.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final StaffRepository staffRepository;

    public Long saveNotice(NoticeFormDto noticeFormDto) throws Exception{
        Notice notice = noticeFormDto.creatNotice();
        Staff staff = staffRepository.findById(noticeFormDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        notice.setStaff(staff);
        noticeRepository.save(notice);
        return notice.getId();
    }
}
