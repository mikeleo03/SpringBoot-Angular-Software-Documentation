package com.example.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.customer.dto.CustomerDTO;
import com.example.customer.dto.CustomerProductDTO;
import com.example.customer.dto.CustomerProductSaveDTO;
import com.example.customer.dto.CustomerSaveDTO;
import com.example.customer.dto.ProductDTO;
import com.example.customer.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves a paginated list of all Customers.
     *
     * @param page The page number to retrieve (defaults to 0).
     * @param size The number of customers per page (defaults to 20).
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link CustomerDTO} objects representing the retrieved customers.
     * @apiNote If no customers are found, a {@link ResponseEntity} with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Retrieve all Customers with pagination.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "Customers not found")
    })
    @GetMapping
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerDTO> customers = customerService.getAllCustomers(pageable);

        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(customers);
    }

    /**
     * Retrieves a Customer by its ID.
     *
     * @param id The ID of the customer to retrieve.
     * @return A {@link ResponseEntity} containing a {@link CustomerDTO} object representing the retrieved customer, or a 404 Not Found if the customer is not found.
     */
    @Operation(summary = "Retrieve a Customer by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id) {
        CustomerDTO customerDTO = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    /**
     * Creates a new Customer.
     *
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the details of the new customer to be created.
     * @return A {@link ResponseEntity} containing the created {@link CustomerDTO} object and an HTTP status code of 201 (Created) upon successful creation.
     */
    @Operation(summary = "Create a new Customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully")
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customerDTO = customerService.createCustomer(customerSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerDTO);
    }

    /**
     * Updates an existing Customer.
     *
     * @param id The ID of the customer to update.
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the updated customer details.
     * @return A {@link ResponseEntity} containing the updated {@link CustomerDTO} object and an HTTP status code of 200 (OK) upon successful update.
     */
    @Operation(summary = "Update an existing Customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable String id, @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customerDTO = customerService.updateCustomer(id, customerSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    /**
     * Retrieves a list of products associated with a customer.
     *
     * @param id The ID of the customer.
     * @return A {@link ResponseEntity} containing a list of {@link ProductDTO} objects representing the customer's products.
     */
    @Operation(summary = "Retrieve products associated with a customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCustomerId(@PathVariable String id) {
        List<ProductDTO> products = customerService.getProductsByCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    /**
     * Adds a product to a customer's list of products.
     *
     * @param customerProductSaveDTO The {@link CustomerProductSaveDTO} object containing the customer and product information.
     * @return A {@link ResponseEntity} containing the created {@link CustomerProductDTO} object and an HTTP status code of 200 (OK) upon successful creation.
     */
    @Operation(summary = "Add a product to a customer's list of products.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product added to customer successfully"),
        @ApiResponse(responseCode = "404", description = "Customer or product not found")
    })
    @PostMapping("/addProduct")
    public ResponseEntity<CustomerProductDTO> addProductToCustomer(@RequestBody CustomerProductSaveDTO customerProductSaveDTO) {
        CustomerProductDTO productCustomer = customerService.addProductToCustomer(customerProductSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productCustomer);
    }
}

