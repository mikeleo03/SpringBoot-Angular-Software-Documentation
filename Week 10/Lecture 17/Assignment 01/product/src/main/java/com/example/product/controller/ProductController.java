package com.example.product.controller;

import java.util.UUID;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.data.model.Status;
import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductSaveDTO;
import com.example.product.dto.ProductSearchCriteriaDTO;
import com.example.product.dto.ProductShowDTO;
import com.example.product.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Retrieves all Products based on the provided search criteria.
     *
     * @param criteria The search criteria to filter the products.
     * @param page The page number to retrieve. Defaults to 0.
     * @param size The number of products to retrieve per page. Defaults to 20.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link ProductShowDTO} objects representing the retrieved products.
     * @apiNote If no products are found that match the search criteria, a {@link ResponseEntity} with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Retrieve all Products with criteria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "Products not found")
    })
    @GetMapping
    public ResponseEntity<Page<ProductShowDTO>> getProductsByCriteria(ProductSearchCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductShowDTO> products = productService.findByCriteria(criteria, pageable);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    /**
     * Creates a new Product.
     *
     * @param productSaveDTO The ProductSaveDTO object containing the details of the new product to be created.
     * @return A ResponseEntity containing the created ProductDTO object and an HTTP status code of 201 (Created) upon successful creation.
     */
    @Operation(summary = "Create a new Product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Product created successfully")
    })
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductSaveDTO productSaveDTO) {
        ProductDTO productDTO = productService.createProduct(productSaveDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDTO);
    }

    /**
     * Updates an existing Product with the provided ProductSaveDTO object.
     *
     * @param id The unique identifier of the Product to be updated.
     * @param productSaveDTO The ProductSaveDTO object containing the details of the updated Product.
     * @return A ResponseEntity containing the updated ProductDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Product with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product updated successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductSaveDTO productSaveDTO) {
        ProductDTO productDTO = productService.updateProduct(id, productSaveDTO);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    /**
     * Updates an existing Product's status from DEACTIVE to ACTIVE.
     *
     * @param id The unique identifier of the Product to be updated.
     * @return A ResponseEntity containing the updated ProductDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Product with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Product status from DEACTIVE to ACTIVE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully activated"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/active/{id}")
    public ResponseEntity<ProductDTO> updateProductStatusActive(@PathVariable UUID id) {
        ProductDTO productDTO = productService.updateProductStatus(id, Status.ACTIVE);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    /**
     * Updates an existing Product's status from ACTIVE to DEACTIVE.
     *
     * @param id The unique identifier of the Product to be updated.
     * @return A ResponseEntity containing the updated ProductDTO object and an HTTP status code of 200 (OK) upon successful update.
     * @apiNote If the Product with the given ID is not found, a ResponseEntity with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Update existing Product status from ACTIVE to DEACTIVE.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product successfully deactivated"),
        @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/deactive/{id}")
    public ResponseEntity<ProductDTO> updateProductStatusDeactive(@PathVariable UUID id) {
        ProductDTO productDTO = productService.updateProductStatus(id, Status.DEACTIVE);
        return ResponseEntity.status(HttpStatus.OK).body(productDTO);
    }

    /**
     * Reduces the quantity of a product by a specified amount.
     *
     * @param productId The unique identifier of the product to reduce the quantity of.
     * @param quantity The quantity to reduce.
     * @return A {@link ResponseEntity} with status code 200 (OK) upon successful reduction.
     * @apiNote If the product with the given ID is not found, a {@link ResponseEntity} with status code 404 (Not Found) is returned.
     */
    @Operation(summary = "Reduce the quantity of a product.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Product quantity reduced successfully"),
        @ApiResponse(responseCode = "404", description = "Product not found"),
        @ApiResponse(responseCode = "400", description = "Insufficient product quantity")
    })
    @PostMapping("/reduce-quantity")
    public ResponseEntity<Void> reduceProductQuantity(@RequestParam UUID productId, @RequestParam int quantity) {
        productService.reduceProductQuantity(productId, quantity);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Retrieves products purchased by a specific customer.
     *
     * @param customerId The unique identifier of the customer.
     * @return A {@link ResponseEntity} containing a list of {@link ProductDTO} objects representing the purchased products.
     * @apiNote If no products are found for the given customer, a {@link ResponseEntity} with status code 204 (No Content) is returned.
     */
    @Operation(summary = "Retrieve products purchased by a customer.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Products retrieved successfully"),
        @ApiResponse(responseCode = "204", description = "No products found for the customer")
    })
    @GetMapping("/by-customer")
    public ResponseEntity<List<ProductDTO>> getProductsByCustomerId(@RequestParam UUID customerId) {
        List<ProductDTO> products = productService.getProductsByCustomerId(customerId);

        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(products);
    }
}
