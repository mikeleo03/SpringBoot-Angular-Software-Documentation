package com.example.customer.controller;

import com.example.customer.dto.CustomerDTO;
import com.example.customer.dto.CustomerProductDTO;
import com.example.customer.dto.CustomerSaveDTO;
import com.example.customer.dto.ProductDTO;
import com.example.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(Pageable pageable) {
        Page<CustomerDTO> customers = customerService.getAllCustomers(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable UUID id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customerDTO = customerService.createCustomer(customerSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID id, @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, customerSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCustomerId(@PathVariable UUID id) {
        List<ProductDTO> products = customerService.getProductsByCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/add/{customerId}/{productId}")
    public ResponseEntity<CustomerProductDTO> addProductToCustomer(@PathVariable UUID customerId, @PathVariable UUID productId) {
        CustomerProductDTO productCustomer = customerService.addProductToCustomer(customerId, productId);
        return ResponseEntity.status(HttpStatus.OK).body(productCustomer);
    }
}

