package com.example.lecture_9_2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lecture_9_2.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Get all employees from the repository and order them by name ascending
    List<Employee> findAllByOrderByNameAsc();
}