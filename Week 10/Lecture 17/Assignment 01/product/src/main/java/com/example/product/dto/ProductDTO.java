package com.example.product.dto;

import java.util.UUID;

import com.example.product.data.model.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private UUID id;
    private String name;
    private Double price;
    private Status status;
    private Integer quantity;
}
