package com.example.fpt_midterm_pos.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailSaveDTO {
    private UUID productId;
    private Integer quantity;
}
