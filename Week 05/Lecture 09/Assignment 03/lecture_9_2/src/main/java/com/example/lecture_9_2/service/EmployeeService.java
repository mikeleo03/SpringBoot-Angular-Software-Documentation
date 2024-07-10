package com.example.lecture_9_2.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;
import com.lowagie.text.DocumentException;

public interface EmployeeService {
    // Retrieves all employees from the database, sorted by their names in ascending order.
    List<Employee> findAll();

    Page<Employee> findAll(Pageable pageable);

    // Retrieves an employee from the database by their unique identifier.
    Employee findById(String theId);

    // Saves the given employee to the database.
    void save(Employee theEmployee);

    // Deletes an employee from the database by their unique identifier.
    void deleteById(String theId);

    // Uploads a CSV file containing employee data and saves the employees to the database.
    void uploadCsv(MultipartFile file) throws IOException;

    // Generates a PDF document containing the employee data from the provided CSV file.
    byte[] generatePdfFromCsv(MultipartFile file) throws IOException, DocumentException;
}
