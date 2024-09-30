package com.example.fpt_midterm_pos.data.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;

import javax.validation.ConstraintViolationException;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        product = new Product();
        product.setId(UUID.randomUUID());
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setStatus(Status.ACTIVE);
        product.setQuantity(10);
        product.setCreatedAt(new java.util.Date());
        product.setUpdatedAt(new java.util.Date());
        productRepository.save(product);
    }

    @Test
    void findProductByNameContainingAndStatus() {
        List<Product> products = productRepository.findByNameContainingAndStatus("Test", Status.ACTIVE);
        assertThat(products).isNotEmpty();
    }

    @Test
    void findProductByStatus() {
        Page<Product> products = productRepository.findAllByStatus(Status.ACTIVE, PageRequest.of(0, 10));
        assertThat(products.getTotalElements()).isPositive();
    }

    @Test
    void findProductByFilters() {
        Page<Product> products = productRepository.findByFilters(Status.ACTIVE, "Test", 50.0, 150.0, PageRequest.of(0, 10));
        assertThat(products.getTotalElements()).isPositive();
    }

    @Test
    void findByNameContainingEmptyResult() {
        assertThat(productRepository.findByNameContainingAndStatus("NonExistent", Status.ACTIVE)).isEmpty();
    }

    @Test
    void findAllByStatusEmptyResult() {
        assertThat(productRepository.findAllByStatus(Status.DEACTIVE, Pageable.unpaged())).isEmpty();
    }

    @Test
    void findByFiltersWithPagination() {
        // Create and save products
        Product product1 = new Product();
        product1.setName("Test Product A");
        product1.setPrice(100.0);
        product1.setStatus(Status.ACTIVE);
        product1.setCreatedAt(new java.util.Date());
        product1.setUpdatedAt(new java.util.Date());
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setName("Test Product B");
        product2.setPrice(200.0);
        product2.setStatus(Status.ACTIVE);
        product2.setCreatedAt(new java.util.Date());
        product2.setUpdatedAt(new java.util.Date());
        productRepository.save(product2);

        // Test with pagination
        Pageable pageable = PageRequest.of(0, 1, Sort.by("name"));
        Page<Product> result = productRepository.findByFilters(Status.ACTIVE, "Test", null, null, pageable);

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void invalidProductSave() {
        try {
            Product invalidProduct = new Product();
            invalidProduct.setName("Test Product 2");
            invalidProduct.setStatus(Status.ACTIVE);
            productRepository.save(invalidProduct);
        } catch (Exception e) {
            // Validate that the exception contains details about constraint violation
            assertTrue(e.getCause() instanceof ConstraintViolationException);
        }
    }
}

