package com.example.lecture_12.services;

import com.example.lecture_12.data.model.Department;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DepartmentService {
    // Retrieves a paginated list of {@link Department} entities.
    Page<Department> findAll(Pageable pageable);

    // Retrieves an {@link Department} entity by its unique identifier.
    Optional<Department> findById(String deptNo);

    // Saves or updates an {@link Department} entity in the database.
    Department save(Department department);

    // Deletes an {@link Department} entity from the database by its unique identifier.
    void deleteById(String deptNo);
}
