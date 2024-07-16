package com.example.lecture_11.data.model;

import java.time.LocalDate;

import com.example.lecture_11.data.model.composite.DeptEmpId;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "dept_emp")
@IdClass(DeptEmpId.class)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DeptEmp {
    
    @Id
    private Integer empNo;

    @Id
    private String deptNo;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate fromDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate toDate;
}
