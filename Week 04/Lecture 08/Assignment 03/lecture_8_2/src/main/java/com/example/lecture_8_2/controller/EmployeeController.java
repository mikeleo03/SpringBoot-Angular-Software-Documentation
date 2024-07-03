package com.example.lecture_8_2.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecture_8_2.model.Employee;
import com.example.lecture_8_2.repository.EmployeeRepository;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    @Autowired
    private final EmployeeRepository employeeRepository;

    @GetMapping("/ds1")
    public ResponseEntity<List<Employee>> listAllEmployeeFromDataSource1() {
        List<Employee> employees = employeeRepository.findAllFromDS1();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/ds2")
    public ResponseEntity<List<Employee>> listAllEmployeeFromDataSource2() {
        List<Employee> employees = employeeRepository.findAllFromDS2();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/ds1/{id}")
    public ResponseEntity<Employee> findEmployeeByIdFromDataSource1(@PathVariable("id") String id) {
        Optional<Employee> employeeOpt = employeeRepository.findByIdFromDS1(id);
        return employeeOpt.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ds2/{id}")
    public ResponseEntity<Employee> findEmployeeByIdFromDataSource2(@PathVariable("id") String id) {
        Optional<Employee> employeeOpt = employeeRepository.findByIdFromDS2(id);
        return employeeOpt.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> insertEmployee(@RequestBody Employee employee) {
        employeeRepository.save(employee);
        return ResponseEntity.ok(employee);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String id,
                                                   @RequestBody Employee employeeForm) {
        employeeForm.setId(id);
        employeeRepository.update(employeeForm);
        return ResponseEntity.ok(employeeForm);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") String id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}