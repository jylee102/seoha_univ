package com.seohauniv.service;

import com.seohauniv.dto.CourseFormDto;
import com.seohauniv.entity.*;
import com.seohauniv.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final CourseService courseService;

    // 강의실 생성
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    @Transactional(readOnly = true)
    public Long count() {
        return roomRepository.count();
    }

    @Transactional(readOnly = true)
    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }

    // 선택한 강의의 강의시간과 해당 강의실 시간표가 겹치는지 확인
    @Transactional(readOnly = true)
    public boolean checkTimeConflict(CourseFormDto courseFormDto) {
        // 해당 강의실을 이용하는 모든 강의 목록
        List<Course> courses = courseService.findByRoomId(courseFormDto.getRoom());

        for (Course course : courses) {
            for (CourseTime courseTime : course.getSyllabus().getCourseTimes()) { // 이미 신청된 강의실의 이용시간
                for (CourseTime time : courseFormDto.getSyllabus().getCourseTimes()) { // 개설하려는 강의의 강의시간
                    if (time.getDay().equals(courseTime.getDay())) {
                        // 시간 범위가 겹치는지 확인
                        if (!(time.getEndTime().isBefore(courseTime.getStartTime()) || time.getStartTime().isAfter(courseTime.getEndTime()))) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }
}
