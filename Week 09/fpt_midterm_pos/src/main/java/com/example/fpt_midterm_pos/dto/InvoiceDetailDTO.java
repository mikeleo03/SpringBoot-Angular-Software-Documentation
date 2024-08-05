package com.example.fpt_midterm_pos.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailDTO {
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Double price;
    private Double amount;
}
