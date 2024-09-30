package com.example.lecture_9_2.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    // Define all the fields
    @Id
    @Column(name="id")
    private String id;
    private String name;
    private LocalDate dob;
    private String address;
    private String department;
    private int salary;
}