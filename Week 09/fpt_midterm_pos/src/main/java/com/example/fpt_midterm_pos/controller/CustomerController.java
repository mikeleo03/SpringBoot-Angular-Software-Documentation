package com.example.fpt_midterm_pos.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves all customers from the database.
     *
     * @param page The index of the page to retrieve. Defaults to 0.
     * @param size The number of customers to retrieve per page. Defaults to 20.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link CustomerShowDTO} objects representing the customers on the specified page.
     * @apiNote If no customers are found, a {@link ResponseEntity} with status status code 204 (No Content) is returned.
     */
    @Operation(summary = "Retrieve all ACTIVE Customers.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customers retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "Customers not found")
    })
    @GetMapping
    public ResponseEntity<Page<CustomerShowDTO>> getAllCustomer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerShowDTO> customerPage = customerService.findAllActiveCustomer(pageable);

        if (customerPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(customerPage);
    }

    /**
     * Creates a new Customer.
     *
     * @param customerSaveDTO The CustomerSaveDTO object containing the details of the new customer to be created.
     * @return A ResponseEntity containing the newly created CustomerDTO object and an HTTP status code of 201 (Created) upon successful creation.
     */
    @Operation(summary = "Create a new Customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer created successfully")
    })
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customer = customerService.createCustomer(customerSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    /**
     * Updates an existing Customer with the provided CustomerSaveDTO object.
     *
     * @param id The unique identifier of the customer to be updated.
     * @param customerSaveDTO The CustomerSaveDTO object containing the details of the updated customer.
     * @return A ResponseEntity containing the updated CustomerDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Customer with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
        @ApiResponse(responseCode = "204", description = "Customer not found")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable("id") UUID id, @Valid @RequestBody CustomerSaveDTO customerSaveDTO) {
        CustomerDTO customer = customerService.updateCustomer(id, customerSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    /**
     * Updates an existing Customer's status from DEACTIVE to ACTIVE.
     *
     * @param id The unique identifier of the Customer to be updated.
     * @return A ResponseEntity containing the updated CustomerDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Customer with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Customer status from DEACTIVE to ACTIVE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer successfully activated"),
        @ApiResponse(responseCode = "204", description = "Customer not found")
    })
    @PutMapping(value = "/active/{id}")
    public ResponseEntity<CustomerDTO> updateCustomerStatusActive(@PathVariable("id") UUID id) {
        CustomerDTO customer = customerService.updateCustomerStatus(id, Status.ACTIVE);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }

    /**
     * Updates an existing Customer's status from ACTIVE to DEACTIVE.
     *
     * @param id The unique identifier of the Customer to be updated.
     * @return A ResponseEntity containing the updated CustomerDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Customer with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Customer status from ACTIVE to DEACTIVE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Customer successfully deactivated"),
        @ApiResponse(responseCode = "204", description = "Customer not found")
    })
    @PutMapping(value = "/deactive/{id}")
    public ResponseEntity<CustomerDTO> updateCustomerStatusDeactive(@PathVariable("id") UUID id) {
        CustomerDTO customer = customerService.updateCustomerStatus(id, Status.DEACTIVE);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
}
