package com.example.lecture_12.data.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "departments")
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    
    @Id
    @Column(length = 4)
    private String deptNo;

    @Column(length = 40, nullable = false, unique = true)
    private String deptName;
}
