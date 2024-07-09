package com.example.lecture_9_2.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_9_2.model.Employee;
import com.example.lecture_9_2.repository.EmployeeRepository;
import com.example.lecture_9_2.service.EmployeeService;
import com.example.lecture_9_2.utils.FileUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Employee findById(String theId) {
        return employeeRepository.findById(theId).orElseThrow();
    }

    @Override
    public void save(Employee theEmployee) {
        employeeRepository.save(theEmployee);
    }

    @Override
    public void deleteById(String theId) {
        employeeRepository.deleteById(theId);
    }

    @Override
    public void uploadCsv(MultipartFile file) {
        try {
            List<Employee> employees = FileUtils.readEmployeesFromCSV(file);
            employeeRepository.saveAll(employees);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload CSV file: " + e.getMessage());
        }
    }
}
