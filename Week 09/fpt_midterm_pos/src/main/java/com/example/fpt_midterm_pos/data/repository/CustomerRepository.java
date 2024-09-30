package com.example.fpt_midterm_pos.data.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Status;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    // Find customer data by considering the status.
    Page<Customer> findByStatus(Status status, Pageable pageable);
}
