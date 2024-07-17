package com.example.lecture_11.services;

import com.example.lecture_11.data.model.Salary;
import com.example.lecture_11.data.model.composite.SalaryId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface SalaryService {
    // Retrieves a paginated list of {@link Salary} entities.
    Page<Salary> findAll(Pageable pageable);

    // Retrieves an {@link Salary} entity by its unique identifier.
    Optional<Salary> findById(SalaryId id);

    // Saves or updates an {@link Salary} entity in the database.
    Salary saveOrUpdate(Salary salary);

    // Deletes an {@link Salary} entity from the database by its unique identifier.
    void deleteById(SalaryId id);
}
