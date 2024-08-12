package com.example.customer.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.customer.data.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, String> {
    // Custom query methods can be added here
}

