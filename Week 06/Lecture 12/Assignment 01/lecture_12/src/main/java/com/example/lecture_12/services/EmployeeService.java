package com.example.lecture_12.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.lecture_12.data.model.Employee;
import com.example.lecture_12.dto.EmployeeSearchCriteriaDTO;

public interface EmployeeService {
    // Retrieves a paginated list of {@link Employee} entities.
    Page<Employee> findAll(Pageable pageable);

    // Retrieves a paginated list of {@link Employee} entities based on the provided search criteria.
    Page<Employee> findByCriteria(EmployeeSearchCriteriaDTO criteria, Pageable pageable);

    // Retrieves an {@link Employee} entity by its unique identifier.
    Optional<Employee> findById(Integer empNo);

    // Saves or updates an {@link Employee} entity in the database.
    Employee save(Employee employee);

    // Deletes an {@link Employee} entity from the database by its unique identifier.
    void deleteById(Integer empNo);
}
