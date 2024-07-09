package com.example.lecture_9_2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.lecture_9_2.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Get all employees from the repository and order them by name ascending
    List<Employee> findAllByOrderByNameAsc();

    // Retrieves a paginated list of all employees from the database, sorted by their names in ascending order.
    Page<Employee> findAllByOrderByNameAsc(Pageable pageable);
}