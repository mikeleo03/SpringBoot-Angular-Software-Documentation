package com.example.customer.data.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.customer.data.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    // Custom query methods can be added here
}

