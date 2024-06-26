package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.*;
import com.seohauniv.entity.*;
import com.seohauniv.repository.BreakRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BreakService {
    private final BreakRepository breakRepository;
    private final MessageService messageService;

    public Break findById(Long id) {
        return breakRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    // 휴학 신청
    public Long saveBreak(BreakFormDto breakFormDto) {
        Break breaks = breakFormDto.creatBreak();
        breakRepository.save(breaks);
        return breaks.getId();
    }

    // 특정 회원(학생)의 휴학 내역 가져오기
    @Transactional(readOnly = true)
    public List<Break> getBreakInfo(String memberId){
        return breakRepository.findByStudentId(memberId);
    }

    // status가 처리중인 데이터만 가져오기
    @Transactional(readOnly = true)
    public List<Break> getProcessingBreaks(String memberId) {
        return breakRepository.findByStudentMemberIdAndStatus(memberId, ProcedureStatus.PROCESSING);
    }

    // 휴학 취소(삭제)
    public void deleteBreak(Long breakId) {
        //★delete하기 전에 select 를 한번 해준다
        //->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
        Break b = breakRepository.findById(breakId)
                .orElseThrow(EntityNotFoundException::new);

        //delete
        //CasCade 설정을 통해 order의 자식 엔티티에 해당하는 orderItem 도 같이 삭제된다.
        breakRepository.delete(b);
    }

    // 휴학 신청서 리스트(with 페이징 처리)
    public Page<BreakDto> getAllBreakToRead(Pageable pageable, String searchValue) {
       return breakRepository.getBreaks(pageable, searchValue);
    }

    // 휴학 신청서 반려 처리
    public Break refuseBreak(Long id) {
        Break breaks = findById(id);
        breaks.setStatus(ProcedureStatus.REFUSAL);

        Message message = new Message(breaks.getStudent().getMember(), "휴학 반려", breaks.getStudent().getName() + " 님의 휴학 신청이 반려되었습니다.");
        messageService.create(message);

        return breaks;
    }

    // 휴학신청 승인
    public Break approvalBreak(Break abreak) {
        abreak.setStatus(ProcedureStatus.APPROVAL);
        
        Message message = new Message(abreak.getStudent().getMember(), "휴학 승인", abreak.getStudent().getName() + " 님의 휴학 신청이 승인되었습니다.");
        messageService.create(message);

        return breakRepository.save(abreak);
    }
}
