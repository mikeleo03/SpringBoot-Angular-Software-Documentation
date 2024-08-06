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
    
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS = {"name", "price", "quantity" };
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Checks if the provided MultipartFile has an Excel format.
     *
     * @param file The MultipartFile to be checked for Excel format.
     * @return True if the file has an Excel format, false otherwise.
     */
    public static boolean hasExcelFormat(MultipartFile file) {
        return file.getContentType() != null && file.getContentType().equals(TYPE);
    }    

    /**
     * Reads products from an Excel file provided as a MultipartFile.
     *
     * @param file The MultipartFile containing the Excel data.
     * @return A list of ProductSaveDTO objects, each representing a product read from the Excel file.
     * @throws IOException If an error occurs while reading the Excel file.
     */
    public static List<ProductSaveDTO> readProductsFromExcel(MultipartFile file) throws IOException, IllegalArgumentException {
        if (!hasExcelFormat(file)) {
            throw new IllegalArgumentException("Invalid file format. Only Excel files are accepted.");
        }
    
        List<ProductSaveDTO> productSaveDTOs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                ProductSaveDTO productSaveDTO = fromExcel(attributes);
                productSaveDTOs.add(productSaveDTO);
            }
        } catch (IOException e) {
            throw new IOException("Error reading Excel file: " + e.getMessage(), e);
        }
        return productSaveDTOs;
    }    

    /**
     * Converts a string array representing a Excel row into a ProductSaveDTO object.
     *
     * @param attributes The string array containing the Excel row data.
     * @return A ProductSaveDTO object populated with the values from the Excel row.
     * @throws IllegalArgumentException If the length of the attributes array is less than the expected Excel header length.
     */
    public static ProductSaveDTO fromExcel(String[] attributes) {
        // Ensure the length of attributes array matches the Excel header length
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