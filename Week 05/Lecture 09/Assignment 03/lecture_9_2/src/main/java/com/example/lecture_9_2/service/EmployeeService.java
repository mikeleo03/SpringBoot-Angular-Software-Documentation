package com.example.lecture_9_2.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;

public interface EmployeeService {
    // Retrieves all employees from the database, sorted by their names in ascending order.
    List<Employee> findAll();

    // Retrieves a paginated list of all employees from the database, sorted by their names in ascending order.
    Page<Employee> findAllPaginate(Pageable pageable);

    // Retrieves an employee from the database by their unique identifier.
    Employee findById(String theId);

    // Saves the given employee to the database.
    void save(Employee theEmployee);

    // Deletes an employee from the database by their unique identifier.
    void deleteById(String theId);

    // Uploads a CSV file containing employee data and saves the employees to the database.
    void uploadCsvAndStore(MultipartFile file) throws IOException;

    // Retrieves the maximum salary among all employees in the database.
    Optional<Integer> findMaxSalary();

    // Retrieves the minimum salary among all employees in the database.
    Optional<Integer> findMinSalary();

    // Retrieves the average salary among all employees in the database.
    Double findAverageSalary();

    // Retrieves the name of the employee with the highest salary in the database.
    List<String> findEmployeeWithHighestSalary();

    // Retrieves the name of the employee with the lowest salary in the database.
    List<String> findEmployeeWithLowestSalary();
}
