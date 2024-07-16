package com.example.lecture_11.data.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "employees")
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer empNo;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(length = 14, nullable = false)
    private String firstName;

    @Column(length = 16, nullable = false)
    private String lastName;

    @Column(columnDefinition = "enum('M','F')", nullable = false)
    @Enumerated(EnumType.STRING)
    private String gender;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate hireDate;
}
