package com.example.customer.data.repository;

import com.example.customer.data.model.CustomerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, String> {
    @Query("SELECT cp.productId FROM CustomerProduct cp WHERE cp.customerId = :customerId")
    List<String> findProductIdsByCustomerId(@Param("customerId") String customerId);
}

