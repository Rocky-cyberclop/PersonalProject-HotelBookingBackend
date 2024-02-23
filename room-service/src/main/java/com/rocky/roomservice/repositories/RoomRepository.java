package com.rocky.roomservice.repositories;

import com.rocky.roomservice.models.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findRoomByNumberBetween(Integer from, Integer to);

    List<Room> findRoomByNumber(Integer number);
}
