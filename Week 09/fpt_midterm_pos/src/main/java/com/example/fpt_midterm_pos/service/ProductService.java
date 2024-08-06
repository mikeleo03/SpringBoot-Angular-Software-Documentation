package com.example.fpt_midterm_pos.service;

import java.util.List;
import java.util.UUID;

import com.example.fpt_midterm_pos.dto.*;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Status;

public interface ProductService {

    // Find products based on the provided criteria.
    Page<ProductShowDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable);

    // Creating a new product.
    ProductDTO createProduct(@Valid ProductSaveDTO productSaveDTO);

    // Updates an existing product with the provided product details.
    ProductDTO updateProduct(UUID id, @Valid ProductSaveDTO productSaveDTO);

    // Updates the status of an existing product.
    ProductDTO updateProductStatus(UUID id, Status status);

    // Saves a list of products from a Excel file to the database.
    List<ProductDTO> saveProductsFromExcel(MultipartFile file);
}
