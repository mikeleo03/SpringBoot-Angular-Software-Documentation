package com.example.fpt_midterm_pos.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.data.repository.ProductRepository;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import com.example.fpt_midterm_pos.exception.DuplicateStatusException;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.mapper.ProductMapper;
import com.example.fpt_midterm_pos.utils.FileUtils;

public class ProductServiceImplTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private MultipartFile file;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateProduct() {
        ProductSaveDTO dto = new ProductSaveDTO("Product", 100.0, 10);
        Product product = new Product();
        product.setId(UUID.randomUUID());
        ProductDTO productDTO = new ProductDTO(product.getId(), "Product", 100.0, Status.Active, 10);

        when(productMapper.toProduct(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(productDTO);

        ProductDTO result = productService.createProduct(dto);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(productDTO);
    }

    @Test
    void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();
        ProductSaveDTO dto = new ProductSaveDTO("Updated Product", 150.0, 20);
        Product product = new Product();
        product.setId(id);
        product.setName("Old Product");
        ProductDTO updatedProductDTO = new ProductDTO(id, "Updated Product", 150.0, Status.Active, 20);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productMapper.toProduct(dto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ProductDTO result = productService.updateProduct(id, dto);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(updatedProductDTO);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenUpdatingProduct() {
        UUID id = UUID.randomUUID();
        ProductSaveDTO dto = new ProductSaveDTO("Updated Product", 150.0, 20);

        when(productRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.updateProduct(id, dto);
        });
    
        assertThat(exception.getMessage()).contains("Product not found");
    }

    @Test
    void shouldUpdateProductStatus() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.Active);
        ProductDTO updatedProductDTO = new ProductDTO(id, "Product", 100.0, Status.Deactive, 10);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toProductDTO(product)).thenReturn(updatedProductDTO);

        ProductDTO result = productService.updateProductStatus(id, Status.Deactive);

        verify(productRepository, times(1)).save(product);
        assertThat(result).isEqualTo(updatedProductDTO);
    }

    @Test
    void shouldThrowDuplicateStatusExceptionWhenUpdatingProductStatus() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);
        product.setStatus(Status.Active);

        when(productRepository.findById(id)).thenReturn(Optional.of(product));

        DuplicateStatusException exception = assertThrows(DuplicateStatusException.class, () -> {
            productService.updateProductStatus(id, Status.Active);
        });
    
        assertThat(exception.getMessage()).contains("Product status is already Active");
    }

    @Test
    void shouldSaveProductsFromExcel() throws Exception {
        List<ProductSaveDTO> productSaveDTOs = List.of(new ProductSaveDTO("Product", 100.0, 10));
        List<Product> products = List.of(mock(Product.class));
        List<ProductDTO> productDTOs = List.of(new ProductDTO());

        // Mock the Product object
        Product product = products.get(0);
        when(product.getQuantity()).thenReturn(10); // Ensure quantity is not null
        when(product.getStatus()).thenReturn(Status.Active);

        // Mock the InputStream with Excel file content
        try (InputStream inputStream = new ByteArrayInputStream("name,price,quantity\nProduct,100.0,10".getBytes())) {
            // Mock the correct content type for an Excel file
            when(file.getContentType()).thenReturn("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            doReturn(inputStream).when(file).getInputStream();

            // Mock FileUtils.readProductsFromExcel method
            mockStatic(FileUtils.class);
            when(FileUtils.readProductsFromExcel(file)).thenReturn(productSaveDTOs);

            // Mock the productMapper and productRepository methods
            when(productMapper.toProductList(productSaveDTOs)).thenReturn(products);
            when(productRepository.saveAll(products)).thenReturn(products);
            when(productMapper.toProductDTOList(products)).thenReturn(productDTOs);

            // Call the method under test
            List<ProductDTO> result = productService.saveProductsFromExcel(file);

            // Verify the interactions and assert the results
            verify(productRepository, times(1)).saveAll(products);
            assertThat(result).isEqualTo(productDTOs);
        }
    }
}