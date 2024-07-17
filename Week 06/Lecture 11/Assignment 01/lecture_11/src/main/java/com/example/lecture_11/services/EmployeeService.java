package com.example.lecture_11.services;

import com.example.lecture_11.data.model.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface EmployeeService {
    // Retrieves a paginated list of {@link Employee} entities.
    Page<Employee> findAll(Pageable pageable);

    // Retrieves an {@link Employee} entity by its unique identifier.
    Optional<Employee> findById(Integer empNo);

    // Saves or updates an {@link Employee} entity in the database.
    Employee saveOrUpdate(Employee employee);

    // Deletes an {@link Employee} entity from the database by its unique identifier.
    void deleteById(Integer empNo);
}
