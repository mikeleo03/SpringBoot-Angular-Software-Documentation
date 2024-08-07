package com.example.fpt_midterm_pos.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Status;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer customer;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("Test Customer");
        customer.setStatus(Status.Active);
        customer.setPhoneNumber("+62123456789");
        customer.setCreatedAt(new java.util.Date());
        customer.setUpdatedAt(new java.util.Date());
        customerRepository.save(customer);
    }

    @Test
    void findByStatus() {
        Page<Customer> customers = customerRepository.findByStatus(Status.Active, PageRequest.of(0, 10));
        assertThat(customers.getTotalElements()).isGreaterThan(0);
    }

    @Test
    void findByStatusEmptyResult() {
        Page<Customer> customers = customerRepository.findByStatus(Status.Deactive, PageRequest.of(0, 10));
        assertThat(customers.getTotalElements()).isEqualTo(0);
    }
}
