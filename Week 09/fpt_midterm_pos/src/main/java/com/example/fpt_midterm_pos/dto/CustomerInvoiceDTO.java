package com.example.fpt_midterm_pos.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerInvoiceDTO {
    private UUID id;
    private String name;
}
