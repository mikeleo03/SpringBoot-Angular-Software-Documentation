package com.example.customer.data.repository;

import com.example.customer.data.model.CustomerProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CustomerProductRepository extends JpaRepository<CustomerProduct, UUID> {
    @Query("SELECT cp.productId FROM CustomerProduct cp WHERE cp.customerId = :customerId")
    List<UUID> findProductIdsByCustomerId(@Param("customerId") UUID customerId);
}

