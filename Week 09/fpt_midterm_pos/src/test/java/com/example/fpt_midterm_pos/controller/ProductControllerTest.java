package com.example.fpt_midterm_pos.controller;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import com.example.fpt_midterm_pos.exception.GlobalExceptionHandler;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.service.ProductService;

@EnableWebMvc
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void shouldCreateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO(UUID.randomUUID(), "Product", 100.0, Status.Active, 10);

        String requestBody = "{ \"name\": \"Product\", \"price\": 100.0, \"quantity\": 10 }";

        when(productService.createProduct(any(ProductSaveDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"Active\",\"quantity\":10}"));
    }

    @Test
    void shouldFailCreateProductReturnValidationErrors() throws Exception {
        String requestBody = "{ \"name\": \"\", \"price\": -10.0, \"quantity\": -5 }";

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Quantity must be nonnegative\",\"Price must be nonnegative\",\"Name is mandatory\",\"Name can only contain letters and spaces\"]}"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Updated Product", 120.0, Status.Active, 15);

        String requestBody = "{ \"name\": \"Updated Product\", \"price\": 120.0, \"quantity\": 15 }";

        when(productService.updateProduct(any(UUID.class), any(ProductSaveDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Updated Product\",\"price\":120.0,\"status\":\"Active\",\"quantity\":15}"));
    }

    @Test
    void shouldFailUpdateProductDueToNotFound() throws Exception {
        UUID productId = UUID.randomUUID();

        // Convert validProductSaveDTO to JSON string for request body
        String requestBody = "{ \"name\": \"Valid Product\", \"price\": 150.0, \"quantity\": 20 }";

        // Mock the service call to throw ResourceNotFoundException
        when(productService.updateProduct(any(UUID.class), any(ProductSaveDTO.class)))
            .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Product not found\"}"));
    }

    @Test
    void shouldUpdateProductStatusActive() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Product", 100.0, Status.Active, 10);

        when(productService.updateProductStatus(any(UUID.class), any(Status.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/products/active/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"Active\",\"quantity\":10}"));
    }

    @Test
    void shouldFailUpdateProductStatusDueToNotFound() throws Exception {
        UUID productId = UUID.randomUUID();

        when(productService.updateProductStatus(any(UUID.class), any(Status.class)))
            .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/active/" + productId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Product not found\"}"));
    }

    @Test
    void shouldUpdateProductStatusDeactive() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Product", 100.0, Status.Deactive, 10);

        when(productService.updateProductStatus(any(UUID.class), any(Status.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/products/deactive/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"Deactive\",\"quantity\":10}"));
    }

    @Test
    void shouldFailUpdateProductStatusDeactiveDueToNotFound() throws Exception {
        UUID productId = UUID.randomUUID();

        when(productService.updateProductStatus(any(UUID.class), any(Status.class)))
            .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/deactive/" + productId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Product not found\"}"));
    }

    @Test
    void shouldFailUploadExcelDueToInvalidFormat() throws Exception {
        MockMultipartFile fileText = new MockMultipartFile("file", "file.txt", "text/plain", "Invalid content".getBytes());

        when(productService.saveProductsFromExcel(fileText))
            .thenThrow(new IllegalArgumentException("Invalid file format. Only Excel files are accepted."));
        
        mockMvc.perform(multipart("/api/v1/products/upload")
                .file(fileText))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"Invalid file format. Only Excel files are accepted.\"}"));
    }

    @Test
    void shouldUploadExcel() throws Exception {
        List<ProductDTO> productDTOs = List.of(new ProductDTO(UUID.randomUUID(), "Product", 100.0, Status.Active, 10));
        MockMultipartFile fileExcel = new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "name,price,quantity\nProduct,100.0,10".getBytes());

        when(productService.saveProductsFromExcel(fileExcel)).thenReturn(productDTOs);

        mockMvc.perform(multipart("/api/v1/products/upload")
                .file(fileExcel))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"" + productDTOs.get(0).getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"Active\",\"quantity\":10}]"));
    }
}