package com.example.fpt_midterm_pos.dto;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSearchCriteriaDTO {
    private String customerName;
    private UUID customerId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;
    
    private Integer month;
    private String sortByDate;
    private String sortByAmount;
}

