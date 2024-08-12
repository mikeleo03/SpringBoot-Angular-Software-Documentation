package com.example.customer.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProductDTO {
    private UUID customerId;
    private String customerName;
    private UUID productId;
    private String productName;
    private int quantity;
    private Date purchaseDate;
}

