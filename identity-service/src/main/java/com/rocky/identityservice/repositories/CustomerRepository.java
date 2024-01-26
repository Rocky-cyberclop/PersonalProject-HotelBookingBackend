package com.rocky.identityservice.repositories;

import com.rocky.identityservice.models.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, ObjectId> {
    List<Customer> findByEmail(String email);
}
