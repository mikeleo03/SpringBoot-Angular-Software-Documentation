package com.example.lecture_11.data.model.composite;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;

@Data
public class SalaryId implements Serializable {
    private Integer empNo;
    private LocalDate fromDate;
}
