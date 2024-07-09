package com.example.lecture_9_2.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;

public interface EmployeeService {
    // Retrieves all employees from the database, sorted by their names in ascending order.
    List<Employee> findAll();

    // Retrieves an employee from the database by their unique identifier.
    Employee findById(String theId);

    // Saves the given employee to the database.
    void save(Employee theEmployee);

    // Deletes an employee from the database by their unique identifier.
    void deleteById(String theId);

    // Uploads a CSV file containing employee data and saves it to the database.
    void uploadCsv(MultipartFile file);
}
