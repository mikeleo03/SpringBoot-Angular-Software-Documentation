package com.example.lecture_12.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.lecture_12.data.model.Employee;
import com.example.lecture_12.data.repository.EmployeeRepository;
import com.example.lecture_12.dto.EmployeeSearchCriteriaDTO;
import com.example.lecture_12.services.EmployeeService;

import jakarta.persistence.criteria.Predicate;
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
     * Retrieves a paginated list of {@link Employee} entities based on the provided search criteria.
     *
     * @param criteria The criteria object containing fields to filter the search.
     *                 - firstName: Filter by first name with the specified operation (eq, like).
     *                 - lastName: Filter by last name with the specified operation (eq, like).
     *                 - gender: Filter by gender with the specified operation (eq).
     *                 - birthDate: Filter by birth date with the specified operation (eq, gt, lt, geq, leq).
     *                 - hireDate: Filter by hire date with the specified operation (eq, gt, lt, geq, leq).
     *                 - birthMonth: Filter by birth month.
     *                 - birthYear: Filter by birth year.
     *                 - hireMonth: Filter by hire month.
     *                 - hireYear: Filter by hire year.
     *                 - sortBy: Field to sort by.
     *                 - sortOrder: Sort order (asc, desc).
     * @param pageable Pagination and sorting parameters.
     * @return A {@link Page} of {@link Employee} entities that match the specified criteria.
     */
    @Override
    public Page<Employee> findByCriteria(EmployeeSearchCriteriaDTO criteria, Pageable pageable) {
        Specification<Employee> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // First name handling
            if (criteria.getFirstName() != null) {
                if ("like".equalsIgnoreCase(criteria.getFirstNameOperation())) {
                    predicates.add(cb.like(root.get("firstName"), "%" + criteria.getFirstName() + "%"));
                } else {
                    predicates.add(cb.equal(root.get("firstName"), criteria.getFirstName()));
                }
            }

            // Last name handling
            if (criteria.getLastName() != null) {
                if ("like".equalsIgnoreCase(criteria.getLastNameOperation())) {
                    predicates.add(cb.like(root.get("lastName"), "%" + criteria.getLastName() + "%"));
                } else {
                    predicates.add(cb.equal(root.get("lastName"), criteria.getLastName()));
                }
            }

            // Gender handling
            if (criteria.getGender() != null) {
                predicates.add(cb.equal(root.get("gender"), criteria.getGender()));
            }

            // Birth date handling
            if (criteria.getBirthDate() != null) {
                switch (criteria.getBirthDateOperation()) {
                    case "gt" -> predicates.add(cb.greaterThan(root.get("birthDate"), criteria.getBirthDate()));
                    case "lt" -> predicates.add(cb.lessThan(root.get("birthDate"), criteria.getBirthDate()));
                    case "geq" -> predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), criteria.getBirthDate()));
                    case "leq" -> predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), criteria.getBirthDate()));
                    default -> predicates.add(cb.equal(root.get("birthDate"), criteria.getBirthDate()));
                }
            }
            if (criteria.getBirthMonth() != null) {
                predicates.add(cb.equal(cb.function("MONTH", Integer.class, root.get("birthDate")), criteria.getBirthMonth()));
            }
            if (criteria.getBirthYear() != null) {
                predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("birthDate")), criteria.getBirthYear()));
            }

            // Hire date handling
            if (criteria.getHireDate() != null) {
                switch (criteria.getHireDateOperation()) {
                    case "gt" -> predicates.add(cb.greaterThan(root.get("hireDate"), criteria.getHireDate()));
                    case "lt" -> predicates.add(cb.lessThan(root.get("hireDate"), criteria.getHireDate()));
                    case "geq" -> predicates.add(cb.greaterThanOrEqualTo(root.get("hireDate"), criteria.getHireDate()));
                    case "leq" -> predicates.add(cb.lessThanOrEqualTo(root.get("hireDate"), criteria.getHireDate()));
                    default -> predicates.add(cb.equal(root.get("hireDate"), criteria.getHireDate()));
                }
            }
            if (criteria.getHireMonth() != null) {
                predicates.add(cb.equal(cb.function("MONTH", Integer.class, root.get("hireDate")), criteria.getHireMonth()));
            }
            if (criteria.getHireYear() != null) {
                predicates.add(cb.equal(cb.function("YEAR", Integer.class, root.get("hireDate")), criteria.getHireYear()));
            }

            return cb.and(predicates.toArray(Predicate[]::new));
        };

        if (criteria.getSortBy() != null && criteria.getSortOrder() != null) {
            Sort sort = Sort.by(Sort.Direction.fromString(criteria.getSortOrder()), criteria.getSortBy());
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }

        return employeeRepository.findAll(specification, pageable);
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
    public Employee save(Employee employee) {
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
