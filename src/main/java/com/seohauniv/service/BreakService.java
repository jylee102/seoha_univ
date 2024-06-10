package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.BreakFormDto;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Student;
import com.seohauniv.repository.BreakRepository;
import com.seohauniv.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BreakService {
    private final BreakRepository breakRepository;

    public Long saveBreak(BreakFormDto breakFormDto)throws Exception{

        Break breaks = breakFormDto.creatBreak();
        breaks.setStatus(ProcedureStatus.PROCESSING);
        breakRepository.save(breaks);
        return breaks.getId();
    }

    @Transactional(readOnly = true)
    public List<Break> getBreakInfo(String memberId){
        List<Break> breakList = breakRepository.getBreakInfo(memberId);

        return breakList;
    }

    //status 처리중일때 데이터 거르기
    @Transactional(readOnly = true)
    public List<Break> getProcessingBreaks(String memberId) {
        return breakRepository.findByStudentMemberIdAndStatus(memberId, ProcedureStatus.PROCESSING);
    }

    //삭제
    public void deleteBreak(Long breakId) {
        //★delete하기 전에 select 를 한번 해준다
        //->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
        Break b = breakRepository.findById(breakId)
                .orElseThrow(EntityNotFoundException::new);

        //delete
        //CasCade 설정을 통해 order의 자식 엔티티에 해당하는 orderItem 도 같이 삭제된다.
        breakRepository.delete(b);
    }
}
