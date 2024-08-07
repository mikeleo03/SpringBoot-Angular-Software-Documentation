package com.example.fpt_midterm_pos.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import com.example.fpt_midterm_pos.dto.ProductSearchCriteriaDTO;
import com.example.fpt_midterm_pos.dto.ProductShowDTO;
import com.example.fpt_midterm_pos.exception.BadRequestException;
import com.example.fpt_midterm_pos.exception.DuplicateStatusException;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.mapper.ProductMapper;
import com.example.fpt_midterm_pos.utils.FileUtils;

class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    private ProductSearchCriteriaDTO criteria;
    private Pageable pageable;
    private Page<Product> productPage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByCriteria_criteria1() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setStatus(Status.ACTIVE);

        criteria = new ProductSearchCriteriaDTO();
        criteria.setName("Test");
        criteria.setMinPrice(50.0);
        criteria.setMaxPrice(150.0);
        criteria.setSortByName("asc");
        criteria.setSortByPrice("desc");

        pageable = PageRequest.of(0, 10);
        productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findByFilters(any(), any(), any(), any(), any())).thenReturn(productPage);
        when(productMapper.toShowDTO(any())).thenReturn(new ProductShowDTO());

        Page<ProductShowDTO> result = productService.findByCriteria(criteria, pageable);

        assertNotNull(result);
        verify(productRepository).findByFilters(any(), eq("Test"), eq(50.0), eq(150.0), any(Pageable.class));
    }

    @Test
    void testFindByCriteria_criteria2() {
        UUID productId = UUID.randomUUID();
        Product product = new Product();
        product.setId(productId);
        product.setName("Test Product");
        product.setPrice(100.0);
        product.setQuantity(10);
        product.setStatus(Status.ACTIVE);

        criteria = new ProductSearchCriteriaDTO();
        criteria.setName("Test");
        criteria.setMinPrice(50.0);
        criteria.setMaxPrice(150.0);
        criteria.setSortByName("desc");
        criteria.setSortByPrice("asc");

        pageable = PageRequest.of(0, 10);
        productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findByFilters(any(), any(), any(), any(), any())).thenReturn(productPage);
        when(productMapper.toShowDTO(any())).thenReturn(new ProductShowDTO());

        Page<ProductShowDTO> result = productService.findByCriteria(criteria, pageable);

        assertNotNull(result);
        verify(productRepository).findByFilters(any(), eq("Test"), eq(50.0), eq(150.0), any(Pageable.class));
    }

    @Test
    void testCreateProduct() {
        ProductSaveDTO dto = new ProductSaveDTO("Product", 100.0, 10);
        Product product = new Product();
        product.setId(UUID.randomUUID());
        ProductDTO productDTO = new ProductDTO(product.getId(), "Product", 100.0, Status.ACTIVE, 10);

        when(productMapper.toProduct(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(dto);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(productDTO);
    }

    @Test
    void testUpdateProduct() {
        UUID id = UUID.randomUUID();
        ProductSaveDTO dto = new ProductSaveDTO("Updated Product", 150.0, 20);
        Product product = new Product();
        product.setId(id);
        product.setName("Old Product");
        ProductDTO updatedProductDTO = new ProductDTO(id, "Updated Product", 150.0, Status.ACTIVE, 20);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.toProduct(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ProductDTO result = productService.updateProduct(id, dto);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(updatedProductDTO);
    }

    @Test
    void testUpdateProduct_withProductNotExist() {
        UUID id = UUID.randomUUID();
        ProductSaveDTO dto = new ProductSaveDTO("Updated Product", 150.0, 20);

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(id, dto);
        });
    
        assertThat(exception.getMessage()).contains("Product not found");
    }

    @Test
    void testUpdateProductStatus_deactive() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.ACTIVE);
        ProductDTO updatedProductDTO = new ProductDTO(id, "Product", 100.0, Status.DEACTIVE, 10);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ProductDTO result = productService.updateProductStatus(id, Status.DEACTIVE);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(updatedProductDTO);
    }

    @Test
    void testUpdateProductStatus_active() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.DEACTIVE);
        ProductDTO updatedProductDTO = new ProductDTO(id, "Product B", 100.0, Status.ACTIVE, 10);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ProductDTO result = productService.updateProductStatus(id, Status.ACTIVE);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(updatedProductDTO);
    }

    @Test
    void testUpdateProductStatus_active_withProductNotExist() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.DEACTIVE);
        ProductDTO updatedProductDTO = new ProductDTO(id, "Product B", 100.0, Status.ACTIVE, 10);

        when(productRepository.findById(id)).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProductStatus(id, Status.ACTIVE);
        });
    
        assertThat(exception.getMessage()).contains("Product not found");
    }

    @Test
    void testUpdateProductStatus_deactive_withProductNotExist() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.ACTIVE);
        ProductDTO updatedProductDTO = new ProductDTO(id, "Product B", 100.0, Status.DEACTIVE, 10);

        when(productRepository.findById(id)).thenReturn(Optional.empty());
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProductStatus(id, Status.DEACTIVE);
        });
    
        assertThat(exception.getMessage()).contains("Product not found");
    }

    @Test
    void testUpdateProductStatus_throwDuplicateStatus() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.ACTIVE);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        DuplicateStatusException exception = assertThrows(DuplicateStatusException.class, () -> {
            productService.updateProductStatus(id, Status.ACTIVE);
        });
    
        assertThat(exception.getMessage()).contains("Product status is already ACTIVE");
    }

    @Test
    void testSaveProductsFromExcel() throws Exception {
        List<ProductSaveDTO> productSaveDTOs = List.of(new ProductSaveDTO("Product", 100.0, 10));
        List<Product> products = List.of(mock(Product.class));
        List<ProductDTO> productDTOs = List.of(new ProductDTO());

        // Mock the Product object
        Product product = products.get(0);
        when(product.getQuantity()).thenReturn(10); // Ensure quantity is not null
        when(product.getStatus()).thenReturn(Status.ACTIVE);
        // Mock the InputStream with Excel file content
        MockMultipartFile fileExcel = new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "name,price,quantity\nProduct,100.0,10".getBytes());

        // Mock FileUtils.readProductsFromExcel method
        mockStatic(FileUtils.class);
        when(FileUtils.readProductsFromExcel(fileExcel)).thenReturn(productSaveDTOs);

        // Mock the productMapper and productRepository methods
        when(productMapper.toProductList(productSaveDTOs)).thenReturn(products);
        when(productRepository.saveAll(products)).thenReturn(products);
        when(productMapper.toProductDTOList(products)).thenReturn(productDTOs);

        // Call the method under test
        List<ProductDTO> result = productService.saveProductsFromExcel(fileExcel);

        // Verify the interactions and assert the results
        verify(productRepository, times(1)).saveAll(products);
        assertThat(result).isEqualTo(productDTOs);
    }

    @Test
    void testSaveProductsFromExcel_withQuantity0() {
        List<ProductSaveDTO> productSaveDTOs = List.of(new ProductSaveDTO("Product", 100.0, 0));
        List<Product> products = List.of(mock(Product.class));
        List<ProductDTO> productDTOs = List.of(new ProductDTO());

        // Mock the Product object
        Product product = products.get(0);
        when(product.getQuantity()).thenReturn(0);
        when(product.getStatus()).thenReturn(Status.DEACTIVE);

        // Mock the InputStream with Excel file content
        MockMultipartFile fileExcel = new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "name,price,quantity\nProduct,100.0,10".getBytes());

        try (MockedStatic<FileUtils> mockedStatic = mockStatic(FileUtils.class)) {
            // Mock FileUtils.readProductsFromExcel method
            mockedStatic.when(() -> FileUtils.readProductsFromExcel(fileExcel)).thenReturn(productSaveDTOs);

            // Mock the productMapper and productRepository methods
            when(productMapper.toProductList(productSaveDTOs)).thenReturn(products);
            when(productRepository.saveAll(products)).thenReturn(products);
            when(productMapper.toProductDTOList(products)).thenReturn(productDTOs);

            // Call the method under test
            List<ProductDTO> result = productService.saveProductsFromExcel(fileExcel);

            // Verify the interactions and assert the results
            verify(productRepository, times(1)).saveAll(products);
            assertThat(result).isEqualTo(productDTOs);
        }
    }

    @Test
    void testSaveProductsFromExcel_withIllegalArgumentException() {
        MockMultipartFile fileExcel = new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "name,price,quantity\nProduct,100.0,10".getBytes());

        try (MockedStatic<FileUtils> mockedStatic = mockStatic(FileUtils.class)) {
            // Mock FileUtils.readProductsFromExcel method to throw IllegalArgumentException
            mockedStatic.when(() -> FileUtils.readProductsFromExcel(fileExcel)).thenThrow(new IllegalArgumentException("Mocked Illegal Argument Exception"));

            // Call the method under test and assert the exception
            BadRequestException exception = assertThrows(BadRequestException.class, () -> {
                productService.saveProductsFromExcel(fileExcel);
            });

            assertThat(exception.getMessage()).contains("Invalid file format. Only Excel files are accepted.");
        }
    }
}