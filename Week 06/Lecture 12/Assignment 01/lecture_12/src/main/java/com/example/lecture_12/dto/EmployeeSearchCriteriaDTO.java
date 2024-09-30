package com.example.lecture_12.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class EmployeeSearchCriteriaDTO {
    private LocalDate birthDate;
    private Integer birthMonth;
    private Integer birthYear;
    private String birthDateOperation;
    private String firstName;
    private String firstNameOperation;
    private String lastName;
    private String lastNameOperation;
    private String gender;
    private String genderOperation;
    private LocalDate hireDate;
    private Integer hireMonth;
    private Integer hireYear;
    private String hireDateOperation;
    private String sortBy;
    private String sortOrder;
}
