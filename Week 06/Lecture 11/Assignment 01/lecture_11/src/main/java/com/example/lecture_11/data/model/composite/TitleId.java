package com.example.lecture_11.data.model.composite;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Embeddable
@AllArgsConstructor
public class TitleId implements Serializable {
    private Integer empNo;
    private String title;
    private LocalDate fromDate;
}
