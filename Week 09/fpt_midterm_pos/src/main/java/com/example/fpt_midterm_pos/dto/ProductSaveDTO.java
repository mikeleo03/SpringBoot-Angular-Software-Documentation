package com.example.fpt_midterm_pos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveDTO {
    private String name;
    private Double price;
    private Integer quantity;
}
