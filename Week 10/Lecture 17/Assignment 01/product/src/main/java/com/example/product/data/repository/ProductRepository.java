package com.example.product.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.product.data.model.Product;
import com.example.product.data.model.Status;

public interface ProductRepository extends JpaRepository<Product, String> {
        
        // Find all the product that containing name with active status
        List<Product> findByNameContainingAndStatus(String name, Status status);

        // Find all the product with given status
        Page<Product> findAllByStatus(Status status, Pageable pageable);

        // Find all the product with given status and containing name
        Page<Product> findByStatusAndNameContaining(Status status, String name, Pageable pageable);

        // Find all product data from the given filter criteria
        @Query("SELECT p FROM Product p WHERE " +
                "p.status = :status AND " +
                "(:name IS NULL OR p.name LIKE %:name%) AND " +
                "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
                "(:maxPrice IS NULL OR p.price <= :maxPrice)")
        Page<Product> findByFilters(@Param("status") Status status,
                                        @Param("name") String name,
                                        @Param("minPrice") Double minPrice,
                                        @Param("maxPrice") Double maxPrice,
                                        Pageable pageable);
}
