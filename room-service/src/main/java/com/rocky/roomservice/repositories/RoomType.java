package com.rocky.roomservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomType extends MongoRepository<RoomType, String> {
}
