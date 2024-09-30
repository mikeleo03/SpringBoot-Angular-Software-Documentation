package com.example.customer.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.customer.data.model.CustomerProduct;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, String> {
    
    // Find all the product IDs based on the customer ID
    @Query("SELECT cp.productId FROM CustomerProduct cp WHERE cp.customerId = :customerId")
    List<String> findProductIdsByCustomerId(@Param("customerId") String customerId);
}

