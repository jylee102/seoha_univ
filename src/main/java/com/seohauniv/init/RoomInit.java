package com.seohauniv.init;

import com.seohauniv.entity.Room;
import com.seohauniv.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoomInit implements ApplicationRunner {

    private final RoomService roomService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (roomService.count() == 0) {
            int[] roomNos = {101, 102, 103, 104, 105,
                    201, 202, 203, 204, 205,
                    301, 302, 303, 304, 305,
                    501, 502, 504, 503, 505};
            
            for (int roomNo : roomNos) {
                Room room = new Room();
                room.setBuildingName("세종관");
                room.setRoomNo(roomNo);
                roomService.create(room);
            }
        }
    }
}