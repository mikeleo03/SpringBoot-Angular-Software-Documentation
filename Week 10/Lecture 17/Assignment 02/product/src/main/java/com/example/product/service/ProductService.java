package com.example.product.service;

import java.util.List;

import com.example.product.dto.*;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.product.data.model.CustomerProduct;
import com.example.product.data.model.Status;

public interface ProductService {

    // Find products based on the provided criteria.
    Page<ProductShowDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable);

    // Get product by Id
    ProductDTO getProductById(String id);

    // Creating a new product.
    ProductDTO createProduct(@Valid ProductSaveDTO productSaveDTO);

    // Updates an existing product with the provided product details.
    ProductDTO updateProduct(String id, @Valid ProductSaveDTO productSaveDTO);

    // Updates the status of an existing product.
    ProductDTO updateProductStatus(String id, Status status);

    // Reduce the product quantity.
    void reduceProductQuantity(String productId, int quantity);

    // Get list of products based on customer ID
    List<ProductDTO> getProductsByCustomerId(String customerId);

    // Save customer product state
    void saveCustomerProduct(CustomerProduct customerProduct);
}
