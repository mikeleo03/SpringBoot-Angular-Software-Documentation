package com.example.lecture_11.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.lecture_11.data.model.Salary;
import com.example.lecture_11.data.model.composite.SalaryId;
import com.example.lecture_11.data.repository.SalaryRepository;
import com.example.lecture_11.services.SalaryService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SalaryServiceImpl implements SalaryService {
    
    private final SalaryRepository salaryRepository;

    /**
     * Retrieves an {@link Salary} entity by its unique identifier.
     *
     * @param id The unique identifier of the {@link Salary} entity to retrieve.
     * @return An {@link Optional} containing the {@link Salary} entity if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Salary> findById(SalaryId id) {
        return salaryRepository.findById(id);
    }

    /**
     * Saves or updates an {@link Salary} entity in the database.
     *
     * @param salary The {@link Salary} entity to be saved or updated.
     * @return The saved or updated {@link Salary} entity.
     */
    @Override
    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }

    /**
     * Deletes an {@link Salary} entity from the database by its unique identifier.
     *
     * @param id The unique identifier of the {@link Salary} entity to be deleted.
     * @return No return value, as the operation is void.
     */
    @Override
    public void deleteById(SalaryId id) {
        salaryRepository.deleteById(id);
    }
}
