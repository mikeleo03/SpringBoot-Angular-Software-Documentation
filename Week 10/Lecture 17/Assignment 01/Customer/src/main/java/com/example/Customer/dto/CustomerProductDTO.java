package com.example.customer.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProductDTO {
    private String customerId;
    private String customerName;
    private String productId;
    private String productName;
    private int quantity;
    private Date purchaseDate;
}

