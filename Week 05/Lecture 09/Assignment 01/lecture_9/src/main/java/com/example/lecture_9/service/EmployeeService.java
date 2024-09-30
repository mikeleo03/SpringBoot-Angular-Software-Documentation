package com.example.lecture_9.service;

import java.util.List;

import com.example.lecture_9.model.Employee;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int theId);

    void save(Employee theEmployee);

    void deleteById(int theId);
}
