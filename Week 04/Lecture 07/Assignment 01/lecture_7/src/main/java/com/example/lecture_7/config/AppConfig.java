package com.example.lecture_7.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.lecture_7.EmployeeWork;
import com.example.lecture_7.entity.Employee;

@Configuration
public class AppConfig {

    @Bean
    public EmployeeWork employeeWork() {
        return new EmployeeWork();
    }

    // Constructor Injection
    // uncomment this and comment the Setter Injection implementation to demo
    /* @Bean
    public Employee employee(EmployeeWork employeeWork) {
        return new Employee("101", "John Doe", 30, employeeWork);
    } */

    // Setter Injection
    @Bean
    public Employee employee(EmployeeWork employeeWork) {
        Employee employee = new Employee();
        employee.setId("101");
        employee.setName("John Doe");
        employee.setAge(30);
        employee.setEmployeeWork(employeeWork);
        return employee;
    }
}
