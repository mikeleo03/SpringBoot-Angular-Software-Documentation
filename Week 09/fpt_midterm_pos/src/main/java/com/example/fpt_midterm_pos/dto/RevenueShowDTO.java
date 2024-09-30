package com.example.fpt_midterm_pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueShowDTO {
    private int year;
    private int month;
    private int day;
    private Double amount;
}
