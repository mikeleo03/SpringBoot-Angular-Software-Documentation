package com.example.lecture_11.data.model;

import java.time.LocalDate;

import com.example.lecture_11.data.model.composite.SalaryId;

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
@Table(name = "salaries")
@IdClass(SalaryId.class)
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Salary {
    
    @Id
    private Integer empNo;

    @Column(nullable = false)
    private Integer salary;

    @Id
    @Temporal(TemporalType.DATE)
    private LocalDate fromDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate toDate;
}
