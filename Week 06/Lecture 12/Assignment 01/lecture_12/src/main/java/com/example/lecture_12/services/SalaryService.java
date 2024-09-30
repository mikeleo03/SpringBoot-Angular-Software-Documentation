package com.example.lecture_12.services;

import java.util.Optional;

import com.example.lecture_12.data.model.Salary;
import com.example.lecture_12.data.model.composite.SalaryId;

public interface SalaryService {
    // Retrieves an {@link Salary} entity by its unique identifier.
    Optional<Salary> findById(SalaryId id);

    // Saves or updates an {@link Salary} entity in the database.
    Salary save(Salary salary);

    // Deletes an {@link Salary} entity from the database by its unique identifier.
    void deleteById(SalaryId id);
}
