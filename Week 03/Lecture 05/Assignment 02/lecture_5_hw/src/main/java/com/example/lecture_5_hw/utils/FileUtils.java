package com.example.lecture_5_hw.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_5_hw.model.Employee;

public class FileUtils {
    /**
     * Reads employees from a CSV file using manual parsing.
     *
     * @param file The CSV file containing employee data.
     * @return A list of {@link Employee} objects read from the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public static List<Employee> readEmployeesFromCSV(MultipartFile file) throws IOException {
        List<Employee> employees = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] attributes = line.split(",");
                Employee employee = Employee.fromCSV(attributes);
                employees.add(employee);
            }
        } catch (IOException e) {
            throw new IOException("Error reading employee (Manual) " + e);
        } 
        return employees;
    }
}