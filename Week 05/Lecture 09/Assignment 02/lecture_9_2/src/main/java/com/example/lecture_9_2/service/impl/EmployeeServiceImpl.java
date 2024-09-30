package com.example.lecture_9_2.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;
import com.example.lecture_9_2.repository.EmployeeRepository;
import com.example.lecture_9_2.service.EmployeeService;
import com.example.lecture_9_2.utils.FileUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Retrieves all employees from the database, sorted by their names in ascending order.
     * @return a list of all employees in the database, sorted by their names.
     */
    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByNameAsc();
    }

    /**
     * Retrieves a paginated list of all employees from the database, sorted by their names in ascending order.
     *
     * @param pageable the pagination and sorting parameters
     * @return a paginated list of all employees in the database, sorted by their names.
     *         The returned Page object contains the list of employees, the total number of pages, and the total number of elements.
     * @throws IllegalArgumentException if the provided Pageable object is null
     */
    @Override
    public Page<Employee> findAll(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Invalid pagination and sorting parameters: null object");
        }
        return employeeRepository.findAllByOrderByNameAsc(pageable);
    }

    /**
     * Retrieves an employee from the database by their unique identifier.
     *
     * @param theId the unique identifier of the employee to be retrieved
     * @return the employee with the given identifier, or throws an exception if not found
     * @throws IllegalArgumentException if the provided identifier is null or empty
     */
    @Override
    public Employee findById(String theId) {
        if (theId == null || theId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee identifier: null or empty string");
        }
        return employeeRepository.findById(theId).orElseThrow();
    }

    /**
     * Saves the given employee to the database.
     *
     * @param theEmployee the employee object to be saved
     * @throws IllegalArgumentException if the provided employee is null
     */
    @Override
    public void save(Employee theEmployee) {
        if (theEmployee == null) {
            throw new IllegalArgumentException("Invalid employee: null object");
        }
        employeeRepository.save(theEmployee);
    }

    /**
     * Deletes an employee from the database by their unique identifier.
     *
     * @param theId the unique identifier of the employee to be deleted
     * @throws IllegalArgumentException if the provided identifier is null or empty
     */
    @Override
    public void deleteById(String theId) {
        if (theId == null || theId.isEmpty()) {
            throw new IllegalArgumentException("Invalid employee identifier: null or empty string");
        }
        employeeRepository.deleteById(theId);
    }

    /**
     * Uploads a CSV file containing employee data and saves it to the database.
     *
     * @param file the MultipartFile containing the CSV data
     * @throws IOException if an error occurs while reading the CSV file
     * @throws RuntimeException if an error occurs while uploading the CSV file
     */
    @Override
    public void uploadCsv(MultipartFile file) {
        try {
            List<Employee> employees = FileUtils.readEmployeesFromCSV(file);
            employeeRepository.saveAll(employees);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CSV file: " + e.getMessage());
        }
    }
}
