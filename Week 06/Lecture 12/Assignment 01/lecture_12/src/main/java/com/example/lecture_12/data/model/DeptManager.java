package com.example.lecture_12.data.model;

import java.time.LocalDate;

import com.example.lecture_12.data.model.composite.DeptManagerId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "dept_manager")
@NoArgsConstructor
@AllArgsConstructor
public class DeptManager {
    
    @EmbeddedId
    private DeptManagerId id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate fromDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate toDate;
}
