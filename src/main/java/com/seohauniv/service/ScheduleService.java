package com.seohauniv.service;

import com.seohauniv.dto.NoticeFormDto;
import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Notice;
import com.seohauniv.entity.Schedule;
import com.seohauniv.entity.Staff;
import com.seohauniv.repository.ScheduleRepository;
import com.seohauniv.repository.StaffRepository;
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
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final StaffRepository staffRepository;

    public Long saveSchedule(ScheduleFormDto scheduleFormDto) throws Exception{
        Schedule schedule = scheduleFormDto.creatSchedule();
        Staff staff = staffRepository.findById(scheduleFormDto.getMemberId()).orElseThrow(EntityNotFoundException::new);
        schedule.setStaff(staff);
        scheduleRepository.save(schedule);
        return schedule.getId();
    }

    @Transactional(readOnly = true)
    public List<Schedule> getAdminSchedule(ScheduleFormDto scheduleFormDto) {
        List<Schedule> scheduleList = scheduleRepository.getAdminSchedule(scheduleFormDto);
        return scheduleList;
    }

}
