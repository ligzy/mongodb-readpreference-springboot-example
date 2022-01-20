package com.soundai.repository.primary;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.soundai.repository.Customer;

public interface PrimaryCustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);

}

