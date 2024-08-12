package com.example.customer.service;

import com.example.customer.dto.CustomerDTO;
import com.example.customer.dto.CustomerSaveDTO;
import com.example.customer.dto.ProductDTO;
import com.example.customer.dto.CustomerProductDTO;
import com.example.customer.dto.CustomerProductSaveDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    // Get all customers
    Page<CustomerDTO> getAllCustomers(Pageable pageable);

    // Get customer by ID
    CustomerDTO getCustomerById(String id);

    // Create a new customer
    CustomerDTO createCustomer(CustomerSaveDTO customerSaveDTO);

    // Update existing customer
    CustomerDTO updateCustomer(String id, CustomerSaveDTO customerSaveDTO);

    // Add a purchased product for a customer
    CustomerProductDTO addProductToCustomer(CustomerProductSaveDTO customerProductSaveDTO);

    // Get all products bought by a customer
    List<ProductDTO> getProductsByCustomer(String customerId);
}
