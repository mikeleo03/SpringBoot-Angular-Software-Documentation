package com.example.customer.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.customer.client.ProductClient;
import com.example.customer.data.model.Customer;
import com.example.customer.data.model.CustomerProduct;
import com.example.customer.data.repository.CustomerProductRepository;
import com.example.customer.data.repository.CustomerRepository;
import com.example.customer.dto.*;
import com.example.customer.exception.InsufficientQuantityException;
import com.example.customer.exception.ResourceNotFoundException;
import com.example.customer.mapper.CustomerMapper;
import com.example.customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final ProductClient productClient;
    private final CustomerProductRepository customerProductRepository;
    private static final String CUSTOMER_NOT_FOUND = "Customer not found";

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper, ProductClient productClient, CustomerProductRepository customerProductRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.productClient = productClient;
        this.customerProductRepository = customerProductRepository;
    }

    /**
     * Retrieves a paginated list of all Customers.
     *
     * @param pageable The pagination information, including the page number and size.
     * @return A page of {@link CustomerDTO} objects representing the retrieved customers.
     */
    @Override
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(customerMapper::toCustomerDTO);
    }

    /**
     * Retrieves a Customer by its unique identifier.
     *
     * @param id The unique identifier of the customer to retrieve.
     * @return A {@link CustomerDTO} representing the retrieved customer.
     * @throws ResourceNotFoundException If the customer with the given ID is not found.
     */
    @Override
    public CustomerDTO getCustomerById(String id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND));
        return customerMapper.toCustomerDTO(customer);
    }

    /**
     * Creates a new Customer.
     *
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the details of the new customer to be created.
     * @return A {@link CustomerDTO} representing the newly created customer.
     */
    @Override
    public CustomerDTO createCustomer(CustomerSaveDTO customerSaveDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerSaveDTO.getFirstName());
        customer.setLastName(customerSaveDTO.getLastName());
        customer.setEmail(customerSaveDTO.getEmail());
        customer.setCreatedAt(new Date());
        customer.setUpdatedAt(new Date());
        customer.setId(UUID.randomUUID().toString());
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(savedCustomer);
    }

    /**
     * Updates an existing Customer.
     *
     * @param id The unique identifier of the customer to update.
     * @param customerSaveDTO The {@link CustomerSaveDTO} object containing the updated customer details.
     * @return A {@link CustomerDTO} representing the updated customer.
     * @throws ResourceNotFoundException If the customer with the given ID is not found.
     */
    @Override
    public CustomerDTO updateCustomer(String id, CustomerSaveDTO customerSaveDTO) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND));

        customer.setFirstName(customerSaveDTO.getFirstName());
        customer.setLastName(customerSaveDTO.getLastName());
        customer.setEmail(customerSaveDTO.getEmail());
        customer.setUpdatedAt(new Date());
        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(updatedCustomer);
    }

    /**
     * Adds a product to a customer's list of products.
     *
     * @param customerProductSaveDTO The {@link CustomerProductSaveDTO} object containing the customer and product information.
     * @return A {@link CustomerProductDTO} representing the newly created customer-product relationship.
     * @throws InsufficientQuantityException If the product's quantity is insufficient.
     * @throws ResourceNotFoundException If the customer or product is not found.
     */
    @Override
    @Transactional
    public CustomerProductDTO addProductToCustomer(CustomerProductSaveDTO customerProductSaveDTO) {
        // Get IDs
        String customerId = customerProductSaveDTO.getCustomerId();
        String productId = customerProductSaveDTO.getProductId();
        int quantity = customerProductSaveDTO.getQuantity();

        // Validate and retrieve the customer
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException(CUSTOMER_NOT_FOUND));

        // Retrieve the product from Product service using WebClient
        ProductDTO product = productClient.getProductById(productId);

        // Check if product is available in sufficient quantity
        if (product.getQuantity() <= 0) {
            throw new InsufficientQuantityException("Insufficient quantity for product: " + product.getName());
        }

        // Update the product quantity in Product service
        productClient.reduceProductQuantity(productId, quantity);

        // Save the customer-product relation
        CustomerProduct customerProduct = new CustomerProduct();
        customerProduct.setCustomerId(customerId);
        customerProduct.setProductId(productId);
        customerProduct.setQuantity(quantity);
        customerProduct.setPurchaseDate(new Date());
        customerProduct.setId(UUID.randomUUID().toString());

        customerProductRepository.save(customerProduct);

        // Send request to Product service to update CustomerProduct data
        productClient.saveCustomerProduct(customerProduct);

        // Prepare the DTO to return
        CustomerProductDTO customerProductDTO = new CustomerProductDTO();
        customerProductDTO.setCustomerId(customerId);
        customerProductDTO.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
        customerProductDTO.setProductId(productId);
        customerProductDTO.setProductName(product.getName());
        customerProductDTO.setQuantity(quantity);
        customerProductDTO.setPurchaseDate(new Date());

        return customerProductDTO;
    }

    /**
     * Retrieves a list of products bought by a customer.
     *
     * @param id The ID of the customer.
     * @return A list of {@link ProductDTO} objects representing the customer's products.
     */
    @Override
    public List<ProductDTO> getProductsByCustomer(String customerId) {
        List<String> productIds = customerProductRepository.findProductIdsByCustomerId(customerId);

        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }

        return productClient.getProductsByCustomerId(customerId); // Fetch details using ProductClient
    }
}
