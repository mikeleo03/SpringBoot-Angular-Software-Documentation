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

    // Retrieves a paginated list of all Customers
    Page<CustomerDTO> getAllCustomers(Pageable pageable);

    // Retrieves a Customer by its unique identifier
    CustomerDTO getCustomerById(String id);

    // Create a new customer
    CustomerDTO createCustomer(CustomerSaveDTO customerSaveDTO);

    // Update existing customer
    CustomerDTO updateCustomer(String id, CustomerSaveDTO customerSaveDTO);

    // Adds a product to a customer's list of products
    CustomerProductDTO addProductToCustomer(CustomerProductSaveDTO customerProductSaveDTO);

    // Retrieves a list of products bought by a customer
    List<ProductDTO> getProductsByCustomer(String customerId);
}
