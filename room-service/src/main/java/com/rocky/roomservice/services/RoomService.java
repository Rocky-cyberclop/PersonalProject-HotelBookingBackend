package com.rocky.roomservice.services;

import com.rocky.roomservice.dtos.ChooseRoomOldConceptRequest;
import com.rocky.roomservice.dtos.RoomWrapper;
import com.rocky.roomservice.dtos.SuggestRoomsResponse;
import com.rocky.roomservice.models.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {
//    This function shall be used one time
    public void createRoom();

    public List<RoomWrapper> getRoomsByFloor(Integer floor);

    public RoomWrapper getByNumber(Integer number);

    SuggestRoomsResponse getSuggestRooms(ChooseRoomOldConceptRequest filter);
}
