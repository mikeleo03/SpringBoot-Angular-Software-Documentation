package com.example.lecture_11.services.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.lecture_11.data.model.Department;
import com.example.lecture_11.data.repository.DepartmentRepository;
import com.example.lecture_11.services.DepartmentService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;

    /**
     * Retrieves a paginated list of {@link Department} entities.
     *
     * @param pageable The pagination and sorting parameters.
     * @return A {@link Page} of {@link Department} entities.
     */
    @Override
    public Page<Department> findAll(Pageable pageable) {
        return departmentRepository.findAll(pageable);
    }

    /**
     * Retrieves an {@link Department} entity by its unique identifier.
     *
     * @param deptNo The unique identifier of the {@link Department} entity to retrieve.
     * @return An {@link Optional} containing the {@link Department} entity if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Department> findById(String deptNo) {
        return departmentRepository.findById(deptNo);
    }

    /**
     * Saves or updates an {@link Department} entity in the database.
     *
     * @param department The {@link Department} entity to be saved or updated.
     * @return The saved or updated {@link Department} entity.
     */
    @Override
    public Department saveOrUpdate(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * Deletes an {@link Department} entity from the database by its unique identifier.
     *
     * @param deptNo The unique identifier of the {@link Department} entity to be deleted.
     * @return No return value, as the operation is void.
     */
    @Override
    public void deleteById(String deptNo) {
        departmentRepository.deleteById(deptNo);
    }
}
