package com.example.fpt_midterm_pos.service.impl;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.CustomerRepository;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.exception.DuplicateStatusException;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.mapper.CustomerMapper;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private Pageable pageable;
    private Page<Customer> customerPage;

    private static final String CUSTOMER_NOT_FOUND = "Customer not found";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAllActiveCustomer() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");
        customer.setStatus(Status.ACTIVE);
        customer.setPhoneNumber("+62123456789");
        customer.setCreatedAt(new java.util.Date());
        customer.setUpdatedAt(new java.util.Date());

        CustomerShowDTO customerShowDTO = new CustomerShowDTO(customer.getId(), customer.getName(), customer.getPhoneNumber());

        pageable = PageRequest.of(0, 10);
        customerPage = new PageImpl<>(Collections.singletonList(customer));

        when(customerRepository.findByStatus(Status.ACTIVE, pageable)).thenReturn(customerPage);
        when(customerMapper.toCustomerShowDTO(customer)).thenReturn(customerShowDTO);

        Page<CustomerShowDTO> result = customerService.findAllActiveCustomer(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(customerShowDTO, result.getContent().get(0));
    }

    @Test
    void testFindById_withCustomerExist() {
        UUID customerId = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("Test Customer");
        customer.setStatus(Status.ACTIVE);
        customer.setPhoneNumber("+62123456789");
        customer.setCreatedAt(new java.util.Date());
        customer.setUpdatedAt(new java.util.Date());

        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        Customer result = customerService.findById(customer.getId());

        assertNotNull(result);
        assertEquals(customer.getId(), result.getId());
        assertEquals(customer.getName(), result.getName());
    }

    @Test
    void testFindById_withCustomerNotExist() {
        UUID customerId = UUID.randomUUID();

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.findById(customerId);
        });

        assertEquals(CUSTOMER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testCreateCustomer() {
        CustomerSaveDTO dto = new CustomerSaveDTO("Test Customer", "+62123456789");
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        CustomerDTO customerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), Status.ACTIVE);

        when(customerMapper.toCustomer(dto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toCustomerDTO(customer)).thenReturn(customerDTO);

        CustomerDTO result = customerService.createCustomer(dto);

        verify(customerRepository, times(1)).save(customer);
        assertThat(result).isEqualTo(customerDTO);
    }

    @Test
    void testUpdateCustomer() {
        UUID id = UUID.randomUUID();
        CustomerSaveDTO dto = new CustomerSaveDTO("Test Customer", "+62123456789");
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Old Customer");
        CustomerDTO updatedCustomerDTO = new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), Status.ACTIVE);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerMapper.toCustomer(dto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toCustomerDTO(customer)).thenReturn(updatedCustomerDTO);

        CustomerDTO result = customerService.updateCustomer(id, dto);

        verify(customerRepository, times(1)).save(customer);
        assertThat(result).isEqualTo(updatedCustomerDTO);
    }

    @Test
    void testUpdateCustomer_withCustomerNotExist() {
        UUID id = UUID.randomUUID();
        CustomerSaveDTO dto = new CustomerSaveDTO("Test Customer", "+62123456789");

        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomer(id, dto);
        });
    
        assertThat(exception.getMessage()).contains(CUSTOMER_NOT_FOUND);
    }

    @Test
    void testUpdateCustomerStatus_deactive() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(Status.ACTIVE);
        CustomerDTO updatedCustomerDTO = new CustomerDTO(id, "Customer", "+62123456789", Status.DEACTIVE);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toCustomerDTO(customer)).thenReturn(updatedCustomerDTO);

        CustomerDTO result = customerService.updateCustomerStatus(id, Status.DEACTIVE);

        verify(customerRepository, times(1)).save(customer);
        assertThat(result).isEqualTo(updatedCustomerDTO);
    }

    @Test
    void testUpdateCustomerStatus_active() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(Status.DEACTIVE);  // Customer starts as DEACTIVE
        CustomerDTO updatedCustomerDTO = new CustomerDTO(id, "Customer", "+62123456789", Status.ACTIVE);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toCustomerDTO(customer)).thenReturn(updatedCustomerDTO);

        CustomerDTO result = customerService.updateCustomerStatus(id, Status.ACTIVE);

        verify(customerRepository, times(1)).save(customer);
        assertThat(result).isEqualTo(updatedCustomerDTO);
    }

    @Test
    void testUpdateCustomerStatus_active_withDuplicateStatus() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(Status.ACTIVE);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        DuplicateStatusException exception = assertThrows(DuplicateStatusException.class, () -> {
            customerService.updateCustomerStatus(id, Status.ACTIVE);
        });
    
        assertThat(exception.getMessage()).contains("Customer status is already ACTIVE");
    }

    @Test
    void testUpdateCustomerStatus_deactive_withDuplicateStatus() {
        UUID id = UUID.randomUUID();
        Customer customer = new Customer();
        customer.setId(id);
        customer.setStatus(Status.DEACTIVE);

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));

        DuplicateStatusException exception = assertThrows(DuplicateStatusException.class, () -> {
            customerService.updateCustomerStatus(id, Status.DEACTIVE);
        });
    
        assertThat(exception.getMessage()).contains("Customer status is already DEACTIVE");
    }

    @Test
    void testUpdateCustomerStatus_active_withCustomerNotExist() {
        UUID id = UUID.randomUUID();
        
        // When customer does not exist
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Expecting ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomerStatus(id, Status.ACTIVE);
        });

        assertThat(exception.getMessage()).contains(CUSTOMER_NOT_FOUND);
    }

    @Test
    void testUpdateCustomerStatus_deactive_withCustomerNotExist() {
        UUID id = UUID.randomUUID();
        
        // When customer does not exist
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        // Expecting ResourceNotFoundException
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.updateCustomerStatus(id, Status.DEACTIVE);
        });

        assertThat(exception.getMessage()).contains(CUSTOMER_NOT_FOUND);
    }
}