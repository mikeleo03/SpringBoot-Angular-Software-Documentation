package com.example.customer.service;

import com.example.customer.dto.CustomerDTO;
import com.example.customer.dto.CustomerSaveDTO;
import com.example.customer.dto.ProductDTO;
import com.example.customer.dto.CustomerProductDTO;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    // Get all customers
    Page<CustomerDTO> getAllCustomers(Pageable pageable);

    // Get customer by ID
    CustomerDTO getCustomerById(UUID id);

    // Create a new customer
    CustomerDTO createCustomer(CustomerSaveDTO customerSaveDTO);

    // Update existing customer
    CustomerDTO updateCustomer(UUID id, CustomerSaveDTO customerSaveDTO);

    // Add a purchased product for a customer
    CustomerProductDTO addProductToCustomer(UUID customerId, UUID productId);

    // Get all products bought by a customer
    List<ProductDTO> getProductsByCustomer(UUID customerId);
}
