package com.example.lecture_5_hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lecture_5_hw.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
}