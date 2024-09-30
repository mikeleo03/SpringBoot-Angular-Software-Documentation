package com.example.fpt_midterm_pos.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;

import jakarta.validation.Valid;

public interface CustomerService {

    // Retrieves a paginated list of all customers.
    Page<CustomerShowDTO> findAllActiveCustomer(Pageable pageable);

    // Creating a new customer.
    CustomerDTO createCustomer(@Valid CustomerSaveDTO customerSaveDTO);

    // Updates an existing customer with the provided customer details.
    CustomerDTO updateCustomer(UUID id, @Valid CustomerSaveDTO customerSaveDTO);

    // Updates the status of an existing customer.
    CustomerDTO updateCustomerStatus(UUID id, Status status);

    // Find customer by its id
    Customer findById(UUID id);
}
