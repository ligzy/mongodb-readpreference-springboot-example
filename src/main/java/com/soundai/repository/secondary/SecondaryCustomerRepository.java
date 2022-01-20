package com.soundai.repository.secondary;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.soundai.repository.Customer;

public interface SecondaryCustomerRepository extends MongoRepository<Customer, String> {

    public Customer findByFirstName(String firstName);
    public List<Customer> findByLastName(String lastName);

}

