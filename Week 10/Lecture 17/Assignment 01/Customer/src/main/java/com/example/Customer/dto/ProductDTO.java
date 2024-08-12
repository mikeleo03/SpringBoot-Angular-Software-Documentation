package com.example.customer.dto;

import com.example.customer.data.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private String id;
    private String name;
    private Double price;
    private Status status;
    private Integer quantity;
}
