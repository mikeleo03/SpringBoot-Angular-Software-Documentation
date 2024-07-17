package com.example.lecture_11.data.model.composite;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class SalaryId implements Serializable {
    private Integer empNo;
    private LocalDate fromDate;
}
