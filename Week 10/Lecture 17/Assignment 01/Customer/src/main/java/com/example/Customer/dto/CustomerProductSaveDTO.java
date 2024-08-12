package com.example.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProductSaveDTO {
    private String customerId;
    private String productId;
    private int quantity;
}

