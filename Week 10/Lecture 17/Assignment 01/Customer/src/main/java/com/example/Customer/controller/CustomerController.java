package com.example.customer.controller;

import com.example.customer.dto.CustomerDTO;
import com.example.customer.dto.CustomerProductDTO;
import com.example.customer.dto.CustomerProductSaveDTO;
import com.example.customer.dto.CustomerSaveDTO;
import com.example.customer.dto.ProductDTO;
import com.example.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDTO> customers = customerService.getAllCustomers(pageable);

        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customerDTO = customerService.createCustomer(customerSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable String id, @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, customerSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCustomerId(@PathVariable String id) {
        List<ProductDTO> products = customerService.getProductsByCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("/addProduct")
    public ResponseEntity<CustomerProductDTO> addProductToCustomer(@RequestBody CustomerProductSaveDTO customerProductSaveDTO) {
        CustomerProductDTO productCustomer = customerService.addProductToCustomer(customerProductSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productCustomer);
    }
}

