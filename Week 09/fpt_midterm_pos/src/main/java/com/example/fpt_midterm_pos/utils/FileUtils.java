package com.example.fpt_midterm_pos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.example.fpt_midterm_pos.dto.ProductSaveDTO;

public class FileUtils {
    
    public static final String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String[] HEADERS = { "name", "price", "quantity" };
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private static boolean hasExcelFormat(MultipartFile file) {
        System.out.println("Debugging Content Type in hasExcelFormat: " + file.getContentType()); // Debugging
        return TYPE.equals(file.getContentType());
    }            

    public static List<ProductSaveDTO> readProductsFromExcel(MultipartFile file) throws IOException {
        if (!hasExcelFormat(file)) {
            throw new IllegalArgumentException("Invalid file format. Only Excel files are accepted.");
        }
    
        List<ProductSaveDTO> productSaveDTOs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                if (attributes.length < HEADERS.length) {
                    throw new IOException("Invalid row format");
                }
                ProductSaveDTO productSaveDTO = fromExcel(attributes);
                productSaveDTOs.add(productSaveDTO);
            }
        } catch (IOException e) {
            logger.error("Error reading Excel file: {}", e.getMessage());
            throw new IOException("Error reading Excel file: " + e.getMessage(), e);
        }
        return productSaveDTOs;
    }

    public static ProductSaveDTO fromExcel(String[] attributes) {
        if (attributes.length < HEADERS.length) {
            throw new IllegalArgumentException("Invalid Excel format");
        }
        ProductSaveDTO productSaveDTO = new ProductSaveDTO();
        productSaveDTO.setName(attributes[0]);
        productSaveDTO.setPrice(Double.valueOf(attributes[1]));
        productSaveDTO.setQuantity(Integer.valueOf(attributes[2]));
        return productSaveDTO;
    }
}