package com.example.product.service;

import java.util.UUID;
import java.util.List;

import com.example.product.dto.*;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.product.data.model.Status;

public interface ProductService {

    // Find products based on the provided criteria.
    Page<ProductShowDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable);

    // Creating a new product.
    ProductDTO createProduct(@Valid ProductSaveDTO productSaveDTO);

    // Updates an existing product with the provided product details.
    ProductDTO updateProduct(UUID id, @Valid ProductSaveDTO productSaveDTO);

    // Updates the status of an existing product.
    ProductDTO updateProductStatus(UUID id, Status status);

    // Reduce the product quantity.
    void reduceProductQuantity(UUID productId, int quantity);

    // Get list of products based on customer ID
    List<ProductDTO> getProductsByCustomerId(UUID customerId);
}
