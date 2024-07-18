package com.example.lecture_12.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeSearchCriteriaDTO {
    private LocalDate birthDate;
    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate hireDate;
}
