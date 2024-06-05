package com.seohauniv.repository;

import com.seohauniv.dto.NoticeSearchDto;
import com.seohauniv.dto.ScheduleFormDto;
import com.seohauniv.entity.Schedule;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> getAdminSchedule(ScheduleFormDto scheduleFormDto);
}
