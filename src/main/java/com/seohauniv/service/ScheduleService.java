package com.seohauniv.service;

import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Schedule;
import com.seohauniv.entity.Staff;
import com.seohauniv.repository.ScheduleRepository;
import com.seohauniv.repository.StaffRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;

    // 학사일정 등록
    public Long saveSchedule(ScheduleFormDto scheduleFormDto) {
        Schedule schedule = scheduleFormDto.creatSchedule();
        Staff staff = staffRepository.findById(scheduleFormDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        schedule.setStaff(staff);
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    // 목록
    @Transactional(readOnly = true)
    public List<Schedule> getAdminSchedule(ScheduleFormDto scheduleFormDto) {
        List<Schedule> scheduleList = scheduleRepository.findAllByOrderByStartAsc();
        return scheduleList;
    }

    // 상세페이지
    @Transactional(readOnly = true)
    public ScheduleFormDto getScheduleDtl(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(EntityNotFoundException::new);
        ScheduleFormDto scheduleFormDto = ScheduleFormDto.of(schedule);
        return scheduleFormDto;
    }

    // 수정하기
    public Long updateSchedule(ScheduleFormDto scheduleFormDto) throws Exception{
        Schedule schedule = scheduleRepository.findById(scheduleFormDto.getId()).orElseThrow(EntityNotFoundException::new);

        schedule.updateSchedule(scheduleFormDto);
        return schedule.getId();
    }

    //삭제
    public void deleteSchedule(Long scheduleId) {
        //★delete하기 전에 select 를 한번 해준다
        //->영속성 컨텍스트에 엔티티를 저장한 후 변경 감지를 하도록 하기 위해
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(EntityNotFoundException::new);

        //delete
        //CasCade 설정을 통해 order의 자식 엔티티에 해당하는 orderItem 도 같이 삭제된다.
        scheduleRepository.delete(schedule);
    }
}
