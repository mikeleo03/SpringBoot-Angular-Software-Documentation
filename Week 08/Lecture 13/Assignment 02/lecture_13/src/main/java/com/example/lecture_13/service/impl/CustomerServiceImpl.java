package com.example.lecture_13.service.impl;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.lecture_13.data.model.Customer;
import com.example.lecture_13.data.model.Status;
import com.example.lecture_13.data.repository.CustomerRepository;
import com.example.lecture_13.dto.CustomerDTO;
import com.example.lecture_13.dto.CustomerSaveDTO;
import com.example.lecture_13.dto.CustomerShowDTO;
import com.example.lecture_13.exception.DuplicateStatusException;
import com.example.lecture_13.exception.ResourceNotFoundException;
import com.example.lecture_13.mapper.CustomerMapper;
import com.example.lecture_13.service.CustomerService;

import jakarta.validation.Valid;

@Service
@Validated
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private CustomerRepository customerRepository;

    /**
     * Retrieves a paginated list of all active customers from the repository.
     *
     * @param pageable The pagination parameters, including the page number and size.
     * @return A Page object containing a list of {@link CustomerShowDTO} objects representing the customers on the specified page.
     */
    @Override
    public Page<CustomerShowDTO> findAllActiveCustomer(Pageable pageable) {
        return customerRepository.findByStatus(Status.Active, pageable).map(customerMapper::toCustomerShowDTO);
    }

    /**
     * Retrieves a customer from the repository based on the provided unique identifier.
     *
     * @param customerId The unique identifier of the customer to be retrieved.
     * @return A {@link Customer} object representing the customer with the given ID.
     * @throws ResourceNotFoundException if no customer is found with the given ID.
     */
    @Override
    public Customer findById(UUID customerId) {
        return customerRepository.findById(customerId).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    /**
     * Creates a new customer in the repository and returns the corresponding {@link CustomerDTO} object.
     *
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the details of the new customer to be created.
     * @return A {@link CustomerDTO} object representing the newly created customer.
     */
    @Override
    public CustomerDTO createCustomer(@Valid CustomerSaveDTO customerSaveDTO) {
        Customer customer = customerMapper.toCustomer(customerSaveDTO);
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(savedCustomer);
    }

    /**
     * Updates an existing customer in the repository with the provided details from the {@link CustomerSaveDTO} object.
     *
     * @param id The unique identifier of the customer to be updated.
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the details of the customer to be updated.
     * @return A {@link CustomerDTO} object representing the updated customer.
     * @throws ResourceNotFoundException if the customer with the given ID is not found.
     */
    @Override
    public CustomerDTO updateCustomer(UUID id, @Valid CustomerSaveDTO customerSaveDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        customer.setName(customerSaveDTO.getName());
        customer.setPhoneNumber(customerSaveDTO.getPhoneNumber());
        customer.setUpdatedAt(new Date());
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(updatedCustomer);
    }

    /**
     * Updates the status of an existing customer in the repository.
     *
     * @param id The unique identifier of the customer whose status is to be updated.
     * @param status The new status to be assigned to the customer.
     * @return A {@link CustomerDTO} object representing the updated customer.
     * @throws ResourceNotFoundException if the customer with the given ID is not found.
     * @throws DuplicateStatusException if the customer already has the specified status.
     */
    @Override
    public CustomerDTO updateCustomerStatus(UUID id, Status status) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (customer.getStatus() == status) {
            throw new DuplicateStatusException("Customer status is already " + status);
        }

        customer.setStatus(status);
        customer.setUpdatedAt(new Date());
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(updatedCustomer);
    }

    /**
     * Deletes a customer from the repository based on the provided unique identifier.
     *
     * @param id The unique identifier of the customer to be deleted.
     * @throws ResourceNotFoundException if no customer is found with the given ID.
     */
    @Override
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }
}