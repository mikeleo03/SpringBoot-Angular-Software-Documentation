package com.example.lecture_9_2.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(String theId);

    void save(Employee theEmployee);

    void deleteById(String theId);

    void uploadCsv(MultipartFile file);
}
