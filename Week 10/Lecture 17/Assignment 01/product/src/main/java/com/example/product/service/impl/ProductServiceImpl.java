package com.example.product.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.product.data.model.CustomerProduct;
import com.example.product.data.model.Product;
import com.example.product.data.model.Status;
import com.example.product.data.repository.CustomerProductRepository;
import com.example.product.data.repository.ProductRepository;
import com.example.product.dto.ProductDTO;
import com.example.product.dto.ProductSaveDTO;
import com.example.product.dto.ProductSearchCriteriaDTO;
import com.example.product.dto.ProductShowDTO;
import com.example.product.exception.DuplicateStatusException;
import com.example.product.exception.InsufficientQuantityException;
import com.example.product.exception.ResourceNotFoundException;
import com.example.product.mapper.ProductMapper;
import com.example.product.service.ProductService;

import jakarta.validation.Valid;

@Service
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CustomerProductRepository customerProductRepository;
    private static final String PRODUCT_NOT_FOUND = "Product not found";

    @Autowired
    public ProductServiceImpl(ProductMapper productMapper, ProductRepository productRepository, CustomerProductRepository customerProductRepository) {
        this.productMapper = productMapper;
        this.productRepository = productRepository;
        this.customerProductRepository = customerProductRepository;
    }

    /**
     * Finds products based on the given criteria and sorts them according to the provided sort rules.
     *
     * @param criteria The search criteria containing the product name, minimum and maximum price, and sorting options.
     * @param pageable The pagination information, including the page number and size.
     * @return A page of {@link ProductShowDTO} objects representing the products that match the criteria and are sorted according to the provided rules.
     */
    @Override
    public Page<ProductShowDTO> findByCriteria(ProductSearchCriteriaDTO criteria, Pageable pageable) {
        // Listing all the criteria
        String productName = criteria.getName();
        String sortByName = criteria.getSortByName();
        String sortByPrice = criteria.getSortByPrice();
        Double minPrice = criteria.getMinPrice();
        Double maxPrice = criteria.getMaxPrice();

        // Define the sort rules
        Sort sort = Sort.unsorted();

        if (sortByName != null && !sortByName.isEmpty()) {
            Sort nameSort = Sort.by("name");
            if (sortByName.equalsIgnoreCase("desc")) {
                nameSort = nameSort.descending();
            } else {
                nameSort = nameSort.ascending();
            }
            sort = sort.and(nameSort);
        }

        if (sortByPrice != null && !sortByPrice.isEmpty()) {
            Sort priceSort = Sort.by("price");
            if (sortByPrice.equalsIgnoreCase("desc")) {
                priceSort = priceSort.descending();
            } else {
                priceSort = priceSort.ascending();
            }
            sort = sort.and(priceSort);
        }

        // Set the pageable
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        // Get the product data from the repo
        Page<Product> products = productRepository.findByFilters(Status.ACTIVE, productName, minPrice, maxPrice, sortedPageable);
        return products.map(productMapper::toShowDTO);
    }

    /**
     * Retrieves a product by its unique identifier.
     *
     * @param id The unique identifier of the product to retrieve.
     * @return A {@link ProductDTO} representing the product with its ID and other relevant details.
     * @throws ResourceNotFoundException If the product with the given ID is not found.
     */
    @Override
    public ProductDTO getProductById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));
        return productMapper.toProductDTO(product);
    }

    /**
     * Creates a new product based on the provided {@link ProductSaveDTO} and saves it to the database.
     *
     * @param productSaveDTO The data transfer object containing the details of the new product to be created.
     * @return A {@link ProductDTO} representing the newly created product with its ID and other relevant details.
     */
    @Override
    public ProductDTO createProduct(@Valid ProductSaveDTO productSaveDTO) {
        Product product = productMapper.toProduct(productSaveDTO);
        product.setStatus(Status.ACTIVE); // Ensure the product is set to active when saving
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());
        product.setId(UUID.randomUUID().toString());
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductDTO(savedProduct);
    }

    /**
     * Updates an existing product in the database with the provided details.
     *
     * @param id The unique identifier of the product to be updated.
     * @param productSaveDTO The data transfer object containing the details of the updated product.
     * @return A {@link ProductDTO} representing the updated product with its ID and other relevant details.
     * @throws ResourceNotFoundException If the product with the given ID is not found in the database.
     */
    @Override
    public ProductDTO updateProduct(String id, @Valid ProductSaveDTO productSaveDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));

        product.setName(productSaveDTO.getName());
        product.setPrice(productSaveDTO.getPrice());
        product.setQuantity(productSaveDTO.getQuantity());
        product.setUpdatedAt(new Date());
        Product updateProduct = productRepository.save(product);
        return productMapper.toProductDTO(updateProduct);
    }

    /**
     * Updates the status of a product in the database.
     *
     * @param id The unique identifier of the product to be updated.
     * @param status The new status of the product.
     * @return A {@link ProductDTO} representing the updated product with its ID and other relevant details.
     * @throws ResourceNotFoundException If the product with the given ID is not found in the database.
     * @throws DuplicateStatusException If the product's status is already the same as the provided status.
     */
    @Override
    public ProductDTO updateProductStatus(String id, Status status) {
        Product prodCheck = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));

        if(status == prodCheck.getStatus()) {
            throw new DuplicateStatusException("Product status is already " + status);
        }

        if (prodCheck.getStatus() == Status.ACTIVE) {
            prodCheck.setStatus(Status.DEACTIVE);
        } else if (prodCheck.getStatus() == Status.DEACTIVE) {
            prodCheck.setStatus(Status.ACTIVE);
        }
        prodCheck.setUpdatedAt(new Date());
        Product updatedProduct = productRepository.save(prodCheck);
        return productMapper.toProductDTO(updatedProduct);
    }
    
    /**
     * Reduces the quantity of a product in the database by the specified amount.
     *
     * @param productId The unique identifier of the product to reduce the quantity for.
     * @param quantity The amount by which to reduce the product's quantity.
     * @throws ResourceNotFoundException If the product with the given ID is not found in the database.
     * @throws InsufficientQuantityException If the product's quantity is less than the specified amount.
     */
    @Override
    @Transactional
    public void reduceProductQuantity(String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(PRODUCT_NOT_FOUND));

        if (product.getQuantity() < quantity) {
            throw new InsufficientQuantityException("Insufficient quantity for product: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
    }

    /**
     * Retrieves a list of products associated with a specific customer.
     *
     * @param customerId The unique identifier of the customer whose products are to be retrieved.
     * @return A list of {@link ProductDTO} representing the products associated with the customer.
     */
    @Override
    public List<ProductDTO> getProductsByCustomerId(String customerId) {
        // Fetch the product IDs associated with the customer from a repository or database
        List<String> productIds = customerProductRepository.findProductIdsByCustomerId(customerId);

        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }

        // Fetch the product details for these product IDs
        return productRepository.findAllById(productIds).stream()
                .map(productMapper::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Saves a new customer-product association to the database.
     *
     * @param customerProduct The {@link CustomerProduct} object containing the details of the new association to be saved.
     * @throws IllegalArgumentException If the provided {@link CustomerProduct} object is null.
     */
    @Override
    public void saveCustomerProduct(CustomerProduct customerProduct) {
        if (customerProduct == null) {
            throw new IllegalArgumentException("CustomerProduct object cannot be null.");
        }
        customerProductRepository.save(customerProduct);
    }
}
