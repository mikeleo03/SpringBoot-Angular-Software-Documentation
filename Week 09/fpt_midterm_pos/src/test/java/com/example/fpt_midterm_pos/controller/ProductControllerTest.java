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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
import com.example.fpt_midterm_pos.dto.ProductSearchCriteriaDTO;
import com.example.fpt_midterm_pos.dto.ProductShowDTO;
import com.example.fpt_midterm_pos.exception.GlobalExceptionHandler;
import com.example.fpt_midterm_pos.exception.ResourceNotFoundException;
import com.example.fpt_midterm_pos.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@EnableWebMvc
class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    }

    @Test
    void testGetProductsByCriteria_withProducts() throws Exception {
        // Prepare test data
        Pageable pageable = PageRequest.of(0, 20);
        ProductShowDTO productShowDTO = new ProductShowDTO(UUID.randomUUID(), "Product", 100.0, 10);
        Page<ProductShowDTO> productPage = new PageImpl<>(List.of(productShowDTO), pageable, 1);

        // Mock the service call
        when(productService.findByCriteria(any(ProductSearchCriteriaDTO.class), any(Pageable.class)))
            .thenReturn(productPage);

        // Convert the Page<ProductShowDTO> to a JSON string for comparison
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedJson = objectMapper.writeValueAsString(productPage);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedJson));
    }

    @Test
    void testGetProductsByCriteria_noProducts() throws Exception {
        // Prepare test data
        Pageable pageable = PageRequest.of(0, 20);
        Page<ProductShowDTO> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        // Mock the service call
        when(productService.findByCriteria(any(ProductSearchCriteriaDTO.class), any(Pageable.class)))
            .thenReturn(emptyPage);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                .param("page", "0")
                .param("size", "20")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCreateProduct_withValidFormat() throws Exception {
        ProductDTO productDTO = new ProductDTO(UUID.randomUUID(), "Product", 100.0, Status.ACTIVE, 10);

        String requestBody = "{ \"name\": \"Product\", \"price\": 100.0, \"quantity\": 10 }";

        when(productService.createProduct(any(ProductSaveDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"ACTIVE\",\"quantity\":10}"));
    }

    @Test
    void testCreateProduct_withInvalidFormat() throws Exception {
        String requestBody = "{ \"name\": \"\", \"price\": -10.0, \"quantity\": -5 }";

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"errors\":[\"Quantity must be nonnegative\",\"Price must be nonnegative\",\"Name is mandatory\",\"Name can only contain letters and spaces\"]}"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Updated Product", 120.0, Status.ACTIVE, 15);

        String requestBody = "{ \"name\": \"Updated Product\", \"price\": 120.0, \"quantity\": 15 }";

        when(productService.updateProduct(any(UUID.class), any(ProductSaveDTO.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Updated Product\",\"price\":120.0,\"status\":\"ACTIVE\",\"quantity\":15}"));
    }

    @Test
    void testUpdateProduct_withProductNotExist() throws Exception {
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
    void testUpdateProductStatusActive() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Product", 100.0, Status.ACTIVE, 10);

        when(productService.updateProductStatus(any(UUID.class), any(Status.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/products/active/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"ACTIVE\",\"quantity\":10}"));
    }

    @Test
    void testUpdateProductStatusActive_withProductNotExist() throws Exception {
        UUID productId = UUID.randomUUID();

        when(productService.updateProductStatus(any(UUID.class), any(Status.class)))
            .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/active/" + productId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Product not found\"}"));
    }

    @Test
    void testUpdateProductStatusDeactive() throws Exception {
        UUID productId = UUID.randomUUID();
        ProductDTO productDTO = new ProductDTO(productId, "Product", 100.0, Status.DEACTIVE, 10);

        when(productService.updateProductStatus(any(UUID.class), any(Status.class))).thenReturn(productDTO);

        mockMvc.perform(put("/api/v1/products/deactive/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":\"" + productDTO.getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"DEACTIVE\",\"quantity\":10}"));
    }

    @Test
    void testUpdateProductStatuDeactive_withProductNotExist() throws Exception {
        UUID productId = UUID.randomUUID();

        when(productService.updateProductStatus(any(UUID.class), any(Status.class)))
            .thenThrow(new ResourceNotFoundException("Product not found"));

        mockMvc.perform(put("/api/v1/products/deactive/" + productId))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{\"error\": \"Product not found\"}"));
    }

    @Test
    void testUploadExcel_withInvalidFormat() throws Exception {
        MockMultipartFile fileText = new MockMultipartFile("file", "file.txt", "text/plain", "Invalid content".getBytes());

        when(productService.saveProductsFromExcel(fileText))
            .thenThrow(new IllegalArgumentException("Invalid file format. Only Excel files are accepted."));
        
        mockMvc.perform(multipart("/api/v1/products/upload")
                .file(fileText))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\": \"Invalid file format. Only Excel files are accepted.\"}"));
    }

    @Test
    void testUploadExcel_withValidFormat() throws Exception {
        List<ProductDTO> productDTOs = List.of(new ProductDTO(UUID.randomUUID(), "Product", 100.0, Status.ACTIVE, 10));
        MockMultipartFile fileExcel = new MockMultipartFile("file", "products.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "name,price,quantity\nProduct,100.0,10".getBytes());

        when(productService.saveProductsFromExcel(fileExcel)).thenReturn(productDTOs);

        mockMvc.perform(multipart("/api/v1/products/upload")
                .file(fileExcel))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":\"" + productDTOs.get(0).getId() + "\",\"name\":\"Product\",\"price\":100.0,\"status\":\"ACTIVE\",\"quantity\":10}]"));
    }
}