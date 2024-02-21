package com.rocky.roomservice.repositories;

import com.rocky.roomservice.models.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends MongoRepository<RoomType, String> {
    List<RoomType> findByName(String name);
}
