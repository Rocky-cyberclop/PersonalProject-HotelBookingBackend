package com.rocky.roomservice.services;

import com.rocky.roomservice.dtos.RoomWrapper;

import java.util.List;

public interface RoomService {
//    This function shall be used one time
    public void createRoom();

    public List<RoomWrapper> getRoomsByFloor(Integer floor);

    public RoomWrapper getByNumber(Integer number);
}
