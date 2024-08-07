package com.example.fpt_midterm_pos.controller;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.CustomerDTO;
import com.example.fpt_midterm_pos.dto.CustomerSaveDTO;
import com.example.fpt_midterm_pos.dto.CustomerShowDTO;
import com.example.fpt_midterm_pos.exception.GlobalExceptionHandler;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebMvc
class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void testGetAllCustomer_withCustomers() throws Exception {
        // Prepare test data
        Pageable pageable = PageRequest.of(0, 20);
        CustomerShowDTO customerShowDTO = new CustomerShowDTO(UUID.randomUUID(), "Customer", "+62123456789");
        Page<CustomerShowDTO> customerPage = new PageImpl<>(List.of(customerShowDTO), pageable, 1);

        // Mock the service call
        when(customerService.findAllActiveCustomer(any(Pageable.class)))
            .thenReturn(customerPage);

        // Convert the Page<CustomerShowDTO> to a JSON string for comparison
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(customerPage);

        // Perform the request and verify the response
        mockMvc.perform(get("/api/v1/customers")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetAllCustomer_noCustomers() throws Exception {
        // Prepare test data
        Pageable pageable = PageRequest.of(0, 20);
        Page<CustomerShowDTO> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        // Mock the service call
        when(customerService.findAllActiveCustomer(any(Pageable.class)))
            .thenReturn(emptyPage);

        // Perform the request and verify the response
        mockMvc.perform(get("/api/v1/customers")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateCustomer_withValidFormat() throws Exception {
        CustomerDTO customerDTO = new CustomerDTO(UUID.randomUUID(), "Customer", "+62123456789", Status.ACTIVE);

        String requestBody = "{ \"name\": \"Customer\", \"phoneNumber\": \"+62123456789\" }";

        when(customerService.createCustomer(any(CustomerSaveDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": \"" + customerDTO.getId() + "\", \"name\": \"Customer\", \"phoneNumber\": \"+62123456789\", \"status\": \"ACTIVE\"}"));
    }

    @Test
    void testCreateCustomer_withInvalidFormat() throws Exception {
        String requestBody = "{ \"name\": \"Customer1\", \"phoneNumber\": \"12123456789\" }";

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Name can only contain letters and spaces\",\"Phone number must start with +62 and contain 9 to 13 digits\"]}"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDTO customerDTO = new CustomerDTO(customerId, "Updated Customer", "+62123456789", Status.ACTIVE);

        String requestBody = "{ \"name\": \"Updated Customer\", \"phoneNumber\": \"+62123456789\" }";

        when(customerService.updateCustomer(any(UUID.class), any(CustomerSaveDTO.class))).thenReturn(customerDTO);

        mockMvc.perform(put("/api/v1/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": \"" + customerDTO.getId() + "\", \"name\": \"Updated Customer\", \"phoneNumber\": \"+62123456789\", \"status\": \"ACTIVE\"}"));
    }

    @Test
    void testUpdateCustomer_withCustomerNotExist() throws Exception {
        UUID customerId = UUID.randomUUID();

        // Convert validCustomerSaveDTO to JSON string for request body
        String requestBody = "{ \"name\": \"Updated Customer\", \"phoneNumber\": \"+62123456789\" }";

        // Mock the service call to throw ResourceNotFoundException
        when(customerService.updateCustomer(any(UUID.class), any(CustomerSaveDTO.class)))
            .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(put("/api/v1/customers/" + customerId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Customer not found\"}"));
    }

    @Test
    void testUpdateCustomerStatusActive() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDTO customerDTO = new CustomerDTO(customerId, "Updated Customer", "+62123456789", Status.ACTIVE);

        when(customerService.updateCustomerStatus(any(UUID.class), any(Status.class))).thenReturn(customerDTO);

        mockMvc.perform(put("/api/v1/customers/active/" + customerId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": \"" + customerDTO.getId() + "\", \"name\": \"Updated Customer\", \"phoneNumber\": \"+62123456789\", \"status\": \"ACTIVE\"}"));
    }

    @Test
    void testUpdateCustomerStatusActive_withCustomerNotExist() throws Exception {
        UUID customerId = UUID.randomUUID();

        when(customerService.updateCustomerStatus(any(UUID.class), any(Status.class)))
            .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(put("/api/v1/customers/active/" + customerId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Customer not found\"}"));
    }

    @Test
    void testUpdateCustomerStatusDeactive() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDTO customerDTO = new CustomerDTO(customerId, "Updated Customer", "+62123456789", Status.DEACTIVE);

        when(customerService.updateCustomerStatus(any(UUID.class), any(Status.class))).thenReturn(customerDTO);

        mockMvc.perform(put("/api/v1/customers/deactive/" + customerId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": \"" + customerDTO.getId() + "\", \"name\": \"Updated Customer\", \"phoneNumber\": \"+62123456789\", \"status\": \"DEACTIVE\"}"));
    }

    @Test
    void testUpdateCustomerStatuDeactive_withCustomerNotExist() throws Exception {
        UUID customerId = UUID.randomUUID();

        when(customerService.updateCustomerStatus(any(UUID.class), any(Status.class)))
            .thenThrow(new ResourceNotFoundException("Customer not found"));

        mockMvc.perform(put("/api/v1/customers/deactive/" + customerId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Customer not found\"}"));
    }
}
