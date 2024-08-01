package com.example.webClientDemo.reactive.model;

import com.example.webClientDemo.reactive.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Employee {
    private Integer employeeId;
    private String firstName;
    private String lastName;
    private Integer age;
    private Role role;
}
