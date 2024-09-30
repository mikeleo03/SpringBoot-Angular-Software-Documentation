package com.example.fpt_midterm_pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailsSearchCriteriaDTO {
    private UUID customerId;
    private Integer month;
    private Integer year;
}
