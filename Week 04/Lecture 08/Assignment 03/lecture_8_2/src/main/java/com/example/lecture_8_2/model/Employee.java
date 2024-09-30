package com.example.lecture_8_2.model;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private LocalDate dob;
    private String address;
    private String department;

    public Employee(String id, String name, LocalDate dob, String address, String department) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.department = department;
    }
}