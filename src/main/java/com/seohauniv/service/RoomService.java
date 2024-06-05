package com.seohauniv.service;

import com.seohauniv.entity.Room;
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

    // 강의실 생성
    public Room create(Room room) {
        return roomRepository.save(room);
    }

    public Long count() {
        return roomRepository.count();
    }

    public List<Room> findAllRoom() {
        return roomRepository.findAll();
    }
}
