package com.rocky.identityservice.repositories;

import com.rocky.identityservice.models.Customer;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    List<Customer> findByEmail(String email);

    List<Customer> findCustomerByCodeNotNull();

    Page<Customer> findAll(Pageable pageable);

    @Query("{ $and: [ { $or: [ { 'name': { $regex: ?0, $options: 'i' } }, { 'phone': { $regex: ?1, $options: 'i' } }, { 'email': { $regex: ?2, $options: 'i' } } ] }, { 'email': { $ne: ?3 } } ] }")
    Page<Customer> findCustomersByNameIsLikeOrPhoneIsLikeOrEmailIsLikeAndEmailIsNot(String name, String phone,String email, String admin, Pageable pageable);

}
