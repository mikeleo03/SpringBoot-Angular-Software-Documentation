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
import com.example.lecture_8_2.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/ds1")
    public ResponseEntity<List<Employee>> listAllEmployeeFromDataSource1() {
        List<Employee> employees = employeeService.findAllFromDS1();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/ds2")
    public ResponseEntity<List<Employee>> listAllEmployeeFromDataSource2() {
        List<Employee> employees = employeeService.findAllFromDS2();
        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/ds1/{id}")
    public ResponseEntity<Employee> findEmployeeByIdFromDataSource1(@PathVariable("id") String id) {
        Optional<Employee> employeeOpt = employeeService.findByIdFromDS1(id);
        return employeeOpt.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ds2/{id}")
    public ResponseEntity<Employee> findEmployeeByIdFromDataSource2(@PathVariable("id") String id) {
        Optional<Employee> employeeOpt = employeeService.findByIdFromDS2(id);
        return employeeOpt.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> insertEmployee(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.save(employee);
            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            System.out.println("Transaction failed. All succesful operations will be rolled back.");
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/fail")
    public ResponseEntity<Employee> insertEmployeeFail(@RequestBody Employee employee) {
        try {
            Employee savedEmployee = employeeService.saveFail(employee);
            return ResponseEntity.ok(savedEmployee);
        } catch (Exception e) {
            System.out.println("Transaction failed. All succesful operations will be rolled back.");
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String id, @RequestBody Employee employeeForm) {
        try {
            employeeForm.setId(id);
            Employee updatedEmployee = employeeService.update(employeeForm);
            return ResponseEntity.ok(updatedEmployee);
        } catch (Exception e) {
            System.out.println("Transaction failed. All succesful operations will be rolled back.");
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") String id) {
        try {
            employeeService.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Transaction failed. All succesful operations will be rolled back.");
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}