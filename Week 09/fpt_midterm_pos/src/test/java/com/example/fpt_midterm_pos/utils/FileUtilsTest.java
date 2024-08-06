package com.example.fpt_midterm_pos.utils;

import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FileUtilsTest {

    private MultipartFile mockMultipartFile;
    private final String validContentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final String invalidContentType = "text/plain";

    @BeforeEach
    public void setUp() {
        mockMultipartFile = Mockito.mock(MultipartFile.class);
    }

    @Test
    public void testHasExcelFormat_ValidContentType() {
        MultipartFile multipartFile = new MockMultipartFile(
                "test.xlsx",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                new byte[0]
        );
        System.out.println("ClassLoader: " + FileUtils.class.getClassLoader());

        System.out.println("Testing with valid content type...");
        boolean result = FileUtils.hasExcelFormat(multipartFile);
        System.out.println("Content Type: " + multipartFile.getContentType());
        System.out.println("Result: " + result);
        
        assertTrue(result);
    }

    @Test
    public void testHasExcelFormat_InvalidContentType() {
        MultipartFile multipartFile = new MockMultipartFile(
                "test.txt",
                "test.txt",
                "text/plain",
                new byte[0]
        );

        System.out.println("Testing with invalid content type...");
        boolean result = FileUtils.hasExcelFormat(multipartFile);
        System.out.println("Content Type: " + multipartFile.getContentType());
        System.out.println("Result: " + result);
        
        assertFalse(result);
    }

    @Test
    public void testReadProductsFromExcel_ValidFile() throws IOException {
        String content = "name,price,quantity\nProduct1,10.0,100\nProduct2,20.0,200";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        when(mockMultipartFile.getInputStream()).thenReturn(inputStream);
        when(mockMultipartFile.getContentType()).thenReturn(validContentType);

        List<ProductSaveDTO> products = FileUtils.readProductsFromExcel(mockMultipartFile);
        
        assertEquals(2, products.size());

        ProductSaveDTO product1 = products.get(0);
        assertEquals("Product1", product1.getName());
        assertEquals(10.0, product1.getPrice());
        assertEquals(100, product1.getQuantity());

        ProductSaveDTO product2 = products.get(1);
        assertEquals("Product2", product2.getName());
        assertEquals(20.0, product2.getPrice());
        assertEquals(200, product2.getQuantity());
    }

    @Test
    public void testReadProductsFromExcel_InvalidFileFormat() {
        when(mockMultipartFile.getContentType()).thenReturn(invalidContentType);
        assertThrows(IllegalArgumentException.class, () -> FileUtils.readProductsFromExcel(mockMultipartFile));
    }

    @Test
    public void testReadProductsFromExcel_InvalidRowFormat() throws IOException {
        String content = "name,price,quantity\nProduct1,10.0";
        InputStream inputStream = new ByteArrayInputStream(content.getBytes());
        when(mockMultipartFile.getInputStream()).thenReturn(inputStream);
        when(mockMultipartFile.getContentType()).thenReturn(validContentType);

        assertThrows(IOException.class, () -> FileUtils.readProductsFromExcel(mockMultipartFile));
    }

    @Test
    public void testFromExcel_ValidAttributes() {
        String[] attributes = {"Product1", "10.0", "100"};
        ProductSaveDTO productSaveDTO = FileUtils.fromExcel(attributes);
        assertEquals("Product1", productSaveDTO.getName());
        assertEquals(10.0, productSaveDTO.getPrice());
        assertEquals(100, productSaveDTO.getQuantity());
    }

    @Test
    public void testFromExcel_InvalidAttributes() {
        String[] attributes = {"Product1", "10.0"};
        assertThrows(IllegalArgumentException.class, () -> FileUtils.fromExcel(attributes));
    }
}