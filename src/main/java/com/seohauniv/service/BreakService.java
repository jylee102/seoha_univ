package com.seohauniv.service;

import com.seohauniv.constant.ProcedureStatus;
import com.seohauniv.dto.BreakFormDto;
import com.seohauniv.entity.Break;
import com.seohauniv.entity.Student;
import com.seohauniv.repository.BreakRepository;
import com.seohauniv.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
