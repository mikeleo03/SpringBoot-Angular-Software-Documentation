package com.example.lecture_11.services.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.lecture_11.data.model.Employee;
import com.example.lecture_11.data.repository.EmployeeRepository;
import com.example.lecture_11.services.EmployeeService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    
    private final EmployeeRepository employeeRepository;

    /**
     * Retrieves a paginated list of {@link Employee} entities.
     *
     * @param pageable The pagination and sorting parameters.
     * @return A {@link Page} of {@link Employee} entities.
     */
    @Override
    public Page<Employee> findAll(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    /**
     * Retrieves an {@link Employee} entity by its unique identifier.
     *
     * @param empNo The unique identifier of the {@link Employee} entity to retrieve.
     * @return An {@link Optional} containing the {@link Employee} entity if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Employee> findById(Integer empNo) {
        return employeeRepository.findById(empNo);
    }

    /**
     * Saves or updates an {@link Employee} entity in the database.
     *
     * @param employee The {@link Employee} entity to be saved or updated.
     * @return The saved or updated {@link Employee} entity.
     */
    @Override
    public Employee saveOrUpdate(Employee employee) {
        return employeeRepository.save(employee);
    }

    /**
     * Deletes an {@link Employee} entity from the database by its unique identifier.
     *
     * @param empNo The unique identifier of the {@link Employee} entity to be deleted.
     * @return No return value, as the operation is void.
     */
    @Override
    public void deleteById(Integer empNo) {
        employeeRepository.deleteById(empNo);
    }
}
