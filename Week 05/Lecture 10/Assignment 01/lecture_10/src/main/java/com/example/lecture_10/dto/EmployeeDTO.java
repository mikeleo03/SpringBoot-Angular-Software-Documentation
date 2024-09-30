package com.example.lecture_10.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeDTO {
    private String id;
    private String name;
    private LocalDate dob;
    private String address;
    private String department;
    private String email;
    private String phone;
}