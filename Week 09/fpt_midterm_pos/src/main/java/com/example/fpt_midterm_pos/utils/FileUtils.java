package com.example.fpt_midterm_pos.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.fpt_midterm_pos.dto.ProductSaveDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class FileUtils {
    
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERS = {"name", "price", "quantity" };
    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Checks if the provided MultipartFile has a CSV format.
     *
     * @param file The MultipartFile to be checked for CSV format.
     * @return True if the file has a CSV format, false otherwise.
     */
    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            logger.error("Invalid file type: " + file.getContentType());
            return false;
        }
        return true;
    }

    /**
     * Reads products from a CSV file provided as a MultipartFile.
     *
     * @param file The MultipartFile containing the CSV data.
     * @return A list of ProductSaveDTO objects, each representing a product read from the CSV file.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    public static List<ProductSaveDTO> readProductsFromCSV(MultipartFile file) throws IOException {
        List<ProductSaveDTO> productSaveDTOs = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                ProductSaveDTO productSaveDTO = fromCSV(attributes);
                productSaveDTOs.add(productSaveDTO);
            }
        } catch (IOException e) {
            throw new IOException("Error reading CSV file: " + e.getMessage(), e);
        }
        return productSaveDTOs;
    }

    /**
     * Converts a string array representing a CSV row into a ProductSaveDTO object.
     *
     * @param attributes The string array containing the CSV row data.
     * @return A ProductSaveDTO object populated with the values from the CSV row.
     * @throws IllegalArgumentException If the length of the attributes array is less than the expected CSV header length.
     */
    public static ProductSaveDTO fromCSV(String[] attributes) {
        // Ensure the length of attributes array matches the CSV header length
        if (attributes.length < HEADERS.length) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        ProductSaveDTO productSaveDTO = new ProductSaveDTO();
        productSaveDTO.setName(attributes[0]);
        productSaveDTO.setPrice(Double.valueOf(attributes[1]));
        productSaveDTO.setQuantity(Integer.valueOf(attributes[2]));
        return productSaveDTO;
    }
}