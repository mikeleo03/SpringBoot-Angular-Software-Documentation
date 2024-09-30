package com.example.fpt_midterm_pos.mapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.example.fpt_midterm_pos.data.model.Product;
import com.example.fpt_midterm_pos.data.model.Status;
import com.example.fpt_midterm_pos.dto.ProductDTO;
import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import com.example.fpt_midterm_pos.dto.ProductShowDTO;

class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    public void setUp() {
        productMapper = Mappers.getMapper(ProductMapper.class);
    }

    private Product createProduct() {
        return new Product(
            UUID.randomUUID(),
            "Test Product",
            100.0,
            Status.ACTIVE,
            10,
            new Date(),
            new Date()
        );
    }

    private ProductDTO createProductDTO() {
        return new ProductDTO(
            UUID.randomUUID(),
            "Test Product",
            100.0,
            Status.ACTIVE,
            10
        );
    }

    private ProductShowDTO createProductShowDTO() {
        return new ProductShowDTO(
            UUID.randomUUID(),
            "Test Product",
            100.0,
            10
        );
    }

    private ProductSaveDTO createProductSaveDTO() {
        return new ProductSaveDTO(
            "Test Product",
            100.0,
            10
        );
    }

    private void assertProductEquals(Product product, ProductDTO productDTO) {
        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getPrice(), productDTO.getPrice());
        assertEquals(product.getStatus(), productDTO.getStatus());
        assertEquals(product.getQuantity(), productDTO.getQuantity());
    }

    @Test
    void testToProductDTO() {
        // Arrange
        Product product = createProduct();

        // Act
        ProductDTO productDTO = productMapper.toProductDTO(product);

        // Assert
        assertProductEquals(product, productDTO);
    }

    @Test
    void testToProductDTO_NullInput() {
        // Act
        ProductDTO productDTO = productMapper.toProductDTO(null);

        // Assert
        assertNull(productDTO);
    }

    @Test
    void testToProductFromProductDTO() {
        // Arrange
        ProductDTO productDTO = createProductDTO();

        // Act
        Product product = productMapper.toProduct(productDTO);

        // Assert
        assertEquals(productDTO.getId(), product.getId());
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getPrice(), product.getPrice());
        assertEquals(productDTO.getQuantity(), product.getQuantity());
        // createdAt and updatedAt are ignored, so we won't check them
    }

    @Test
    void testToProductFromProductDTO_NullInput() {
        // Act
        Product product = productMapper.toProduct((ProductDTO) null);

        // Assert
        assertNull(product);
    }

    @Test
    void testToShowDTO() {
        // Arrange
        Product product = createProduct();

        // Act
        ProductShowDTO productShowDTO = productMapper.toShowDTO(product);

        // Assert
        assertEquals(product.getId(), productShowDTO.getId());
        assertEquals(product.getName(), productShowDTO.getName());
        assertEquals(product.getPrice(), productShowDTO.getPrice());
        assertEquals(product.getQuantity(), productShowDTO.getQuantity());
    }

    @Test
    void testToShowDTO_NullInput() {
        // Act
        ProductShowDTO productShowDTO = productMapper.toShowDTO(null);

        // Assert
        assertNull(productShowDTO);
    }

    @Test
    void testToProductFromProductShowDTO() {
        // Arrange
        ProductShowDTO productShowDTO = createProductShowDTO();

        // Act
        Product product = productMapper.toProduct(productShowDTO);

        // Assert
        assertEquals(productShowDTO.getId(), product.getId());
        assertEquals(productShowDTO.getName(), product.getName());
        assertEquals(productShowDTO.getPrice(), product.getPrice());
        assertEquals(productShowDTO.getQuantity(), product.getQuantity());
        // createdAt, updatedAt, and status are ignored, so we won't check them
    }

    @Test
    void testToProductFromProductShowDTO_NullInput() {
        // Act
        Product product = productMapper.toProduct((ProductShowDTO) null);

        // Assert
        assertNull(product);
    }

    @Test
    void testToProductSaveDTO() {
        // Arrange
        Product product = createProduct();

        // Act
        ProductSaveDTO productSaveDTO = productMapper.toProductSaveDTO(product);

        // Assert
        assertEquals(product.getName(), productSaveDTO.getName());
        assertEquals(product.getPrice(), productSaveDTO.getPrice());
        assertEquals(product.getQuantity(), productSaveDTO.getQuantity());
    }

    @Test
    void testToProductSaveDTO_NullInput() {
        // Act
        ProductSaveDTO productSaveDTO = productMapper.toProductSaveDTO(null);

        // Assert
        assertNull(productSaveDTO);
    }

    @Test
    void testToProductFromProductSaveDTO() {
        // Arrange
        ProductSaveDTO productSaveDTO = createProductSaveDTO();

        // Act
        Product product = productMapper.toProduct(productSaveDTO);

        // Assert
        assertEquals(productSaveDTO.getName(), product.getName());
        assertEquals(productSaveDTO.getPrice(), product.getPrice());
        assertEquals(productSaveDTO.getQuantity(), product.getQuantity());
        // id, status, createdAt, and updatedAt are ignored, so we won't check them
    }

    @Test
    void testToProductFromProductSaveDTO_NullInput() {
        // Act
        Product product = productMapper.toProduct((ProductSaveDTO) null);

        // Assert
        assertNull(product);
    }

    @Test
    void testToProductDTOList() {
        // Arrange
        List<Product> products = Arrays.asList(createProduct(), createProduct());

        // Act
        List<ProductDTO> productDTOs = productMapper.toProductDTOList(products);

        // Assert
        assertEquals(products.size(), productDTOs.size());
        for (int i = 0; i < products.size(); i++) {
            assertProductEquals(products.get(i), productDTOs.get(i));
        }
    }

    @Test
    void testToProductDTOList_EmptyList() {
        // Act
        List<ProductDTO> productDTOs = productMapper.toProductDTOList(Collections.emptyList());

        // Assert
        assertTrue(productDTOs.isEmpty());
    }

    @Test
    void testToProductDTOList_NullInput() {
        // Act
        List<ProductDTO> productDTOs = productMapper.toProductDTOList(null);

        // Assert
        assertNull(productDTOs);
    }

    @Test
    void testToProductList() {
        // Arrange
        List<ProductSaveDTO> productSaveDTOs = Arrays.asList(createProductSaveDTO(), createProductSaveDTO());

        // Act
        List<Product> products = productMapper.toProductList(productSaveDTOs);

        // Assert
        assertEquals(productSaveDTOs.size(), products.size());
        for (int i = 0; i < productSaveDTOs.size(); i++) {
            assertEquals(productSaveDTOs.get(i).getName(), products.get(i).getName());
            assertEquals(productSaveDTOs.get(i).getPrice(), products.get(i).getPrice());
            assertEquals(productSaveDTOs.get(i).getQuantity(), products.get(i).getQuantity());
            // id, status, createdAt, and updatedAt are ignored, so we won't check them
        }
    }

    @Test
    void testToProductList_EmptyList() {
        // Act
        List<Product> products = productMapper.toProductList(Collections.emptyList());

        // Assert
        assertTrue(products.isEmpty());
    }

    @Test
    void testToProductList_NullInput() {
        // Act
        List<Product> products = productMapper.toProductList(null);

        // Assert
        assertNull(products);
    }
}
