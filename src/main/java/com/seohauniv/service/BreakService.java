package com.seohauniv.service;

import com.seohauniv.dto.BreakFormDto;
import com.seohauniv.entity.Break;
import com.seohauniv.repository.BreakRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BreakService {
    private final BreakRepository breakRepository;

    public Long saveBreak(BreakFormDto breakFormDto)throws Exception{ //postFormDto 의 정보를 담은 객체를 생성한다. throws Exception: 어딘가로 전송한다
        Break breaks = breakFormDto.creatBreak(); //postFormDto를 기반으로 Post 객체를 생성한다.
        breakRepository.save(breaks); // Jpa 스프링 데이터 인터페이스에서 save(엔티티) 를 저장한다.
        return breaks.getId(); // 저장된 post id를 반화나한다. getId(): 저장된 포스트id를 반환하여 다른 곳에서 활용할수 있다.
    }
}
