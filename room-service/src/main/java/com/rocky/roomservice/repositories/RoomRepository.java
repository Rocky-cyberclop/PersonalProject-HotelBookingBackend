package com.rocky.roomservice.repositories;

import com.rocky.roomservice.models.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findRoomByNumberBetween(Integer from, Integer to);

    List<Room> findRoomByNumber(Integer number);

    @Query("{'roomType.name': { $in: ?0 }, 'roomType.capacity': { $in: ?1 }, 'roomType.utilities': { $in: ?2 }, 'number': { $nin: ?3 }}")
    Page<Room> findRoomByRoomType_NameInAndCapacityInAndUtilitiesInAndNumberNotIn(
            List<String> names, List<Integer> capacities, List<String> utilities, List<Integer> numbers, Pageable pageable);
}
