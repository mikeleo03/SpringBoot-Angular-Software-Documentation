package com.example.fpt_midterm_pos.mapper;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.example.fpt_midterm_pos.data.model.Customer;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerInvoiceDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;

class CustomerMapperTest {

    private CustomerMapper customerMapper;

    @BeforeEach
    public void setUp() {
        customerMapper = Mappers.getMapper(CustomerMapper.class);
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName("John Doe");
        customer.setPhoneNumber("+621234567890");
        customer.setStatus(Status.ACTIVE);
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        return customer;
    }

    private CustomerDTO createCustomerDTO() {
        return new CustomerDTO(
            UUID.randomUUID(), 
            "John Doe", 
            "+621234567890", 
            Status.ACTIVE
        );
    }

    private CustomerShowDTO createCustomerShowDTO() {
        return new CustomerShowDTO(
            UUID.randomUUID(), 
            "John Doe", 
            "+621234567890"
        );
    }

    private CustomerSaveDTO createCustomerSaveDTO() {
        return new CustomerSaveDTO(
            "John Doe", 
            "+621234567890"
        );
    }

    private CustomerInvoiceDTO createCustomerInvoiceDTO() {
        return new CustomerInvoiceDTO(
            UUID.randomUUID(), 
            "John Doe"
        );
    }

    private void assertCustomerEquals(Customer customer, CustomerDTO customerDTO) {
        assertEquals(customer.getId(), customerDTO.getId());
        assertEquals(customer.getName(), customerDTO.getName());
        assertEquals(customer.getPhoneNumber(), customerDTO.getPhoneNumber());
        assertEquals(customer.getStatus(), customerDTO.getStatus());
    }

    @Test
    void testToCustomerDTO() {
        // Arrange
        Customer customer = createCustomer();
        
        // Act
        CustomerDTO customerDTO = customerMapper.toCustomerDTO(customer);

        // Assert
        assertCustomerEquals(customer, customerDTO);
    }

    @Test
    void testToCustomerDTO_NullInput() {
        // Act
        CustomerDTO customerDTO = customerMapper.toCustomerDTO(null);

        // Assert
        assertNull(customerDTO);
    }

    @Test
    void testToCustomerFromCustomerDTO() {
        // Arrange
        CustomerDTO customerDTO = createCustomerDTO();
        
        // Act
        Customer customer = customerMapper.toCustomer(customerDTO);

        // Assert
        assertEquals(customerDTO.getId(), customer.getId());
        assertEquals(customerDTO.getName(), customer.getName());
        assertEquals(customerDTO.getPhoneNumber(), customer.getPhoneNumber());
        assertEquals(customerDTO.getStatus(), customer.getStatus());
        // createdAt, updatedAt, and invoice are ignored, so we won't check them
    }

    @Test
    void testToCustomerFromCustomerDTO_NullInput() {
        // Act
        Customer customer = customerMapper.toCustomer((CustomerDTO) null);

        // Assert
        assertNull(customer);
    }

    @Test
    void testToCustomerShowDTO() {
        // Arrange
        Customer customer = createCustomer();
        
        // Act
        CustomerShowDTO customerShowDTO = customerMapper.toCustomerShowDTO(customer);

        // Assert
        assertEquals(customer.getId(), customerShowDTO.getId());
        assertEquals(customer.getName(), customerShowDTO.getName());
        assertEquals(customer.getPhoneNumber(), customerShowDTO.getPhoneNumber());
    }

    @Test
    void testToCustomerShowDTO_NullInput() {
        // Act
        CustomerShowDTO customerShowDTO = customerMapper.toCustomerShowDTO(null);

        // Assert
        assertNull(customerShowDTO);
    }

    @Test
    void testToCustomerFromShowDTO() {
        // Arrange
        CustomerShowDTO customerShowDTO = createCustomerShowDTO();
        
        // Act
        Customer customer = customerMapper.toCustomer(customerShowDTO);

        // Assert
        assertEquals(customerShowDTO.getId(), customer.getId());
        assertEquals(customerShowDTO.getName(), customer.getName());
        assertEquals(customerShowDTO.getPhoneNumber(), customer.getPhoneNumber());
        // status, createdAt, updatedAt, and invoice are ignored, so we won't check them
    }

    @Test
    void testToCustomerFromShowDTO_NullInput() {        
        // Act
        Customer customer = customerMapper.toCustomer((CustomerShowDTO) null);

        // Assert
        assertNull(customer);
    }

    @Test
    void testToCustomerSaveDTO() {
        // Arrange
        Customer customer = createCustomer();
        
        // Act
        CustomerSaveDTO customerSaveDTO = customerMapper.toCustomerSaveDTO(customer);

        // Assert
        assertEquals(customer.getName(), customerSaveDTO.getName());
        assertEquals(customer.getPhoneNumber(), customerSaveDTO.getPhoneNumber());
    }

    @Test
    void testToCustomerSaveDTO_NullInput() {        
        // Act
        CustomerSaveDTO customerSaveDTO = customerMapper.toCustomerSaveDTO(null);

        // Assert
        assertNull(customerSaveDTO);
    }

    @Test
    void testToCustomerFromSaveDTO() {
        // Arrange
        CustomerSaveDTO customerSaveDTO = createCustomerSaveDTO();
        
        // Act
        Customer customer = customerMapper.toCustomer(customerSaveDTO);

        // Assert
        assertEquals(customerSaveDTO.getName(), customer.getName());
        assertEquals(customerSaveDTO.getPhoneNumber(), customer.getPhoneNumber());
        // id, status, createdAt, updatedAt, and invoice are ignored, so we won't check them
    }

    @Test
    void testToCustomerFromSaveDTO_NullInput() {        
        // Act
        Customer customer = customerMapper.toCustomer((CustomerSaveDTO) null);

        // Assert
        assertNull(customer);
    }

    @Test
    void testToCustomerInvoiceDTO() {
        // Arrange
        Customer customer = createCustomer();
        
        // Act
        CustomerInvoiceDTO customerInvoiceDTO = customerMapper.toCostumerInvoiceDTO(customer);

        // Assert
        assertEquals(customer.getId(), customerInvoiceDTO.getId());
        assertEquals(customer.getName(), customerInvoiceDTO.getName());
    }

    @Test
    void testToCustomerInvoiceDTO_NullInput() {        
        // Act
        CustomerInvoiceDTO customerInvoiceDTO = customerMapper.toCostumerInvoiceDTO(null);

        // Assert
        assertNull(customerInvoiceDTO);
    }

    @Test
    void testToCustomerFromInvoiceDTO() {
        // Arrange
        CustomerInvoiceDTO customerInvoiceDTO = createCustomerInvoiceDTO();
        
        // Act
        Customer customer = customerMapper.toCustomer(customerInvoiceDTO);

        // Assert
        assertNotNull(customer);
        assertEquals(customerInvoiceDTO.getId(), customer.getId());
        assertEquals(customerInvoiceDTO.getName(), customer.getName());
        // phoneNumber, status, createdAt, updatedAt, and invoice are ignored, so we won't check them
    }

    @Test
    void testToCustomerFromInvoiceDTO_NullInput() {
        // Act
        Customer customer = customerMapper.toCustomer((CustomerInvoiceDTO) null);

        // Assert
        assertNull(customer);
    }
}
