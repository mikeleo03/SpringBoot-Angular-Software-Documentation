package com.example.customer.data.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CustomerProduct")
public class CustomerProduct {

    @Id
    @Column(name = "ID", columnDefinition = "VARCHAR(36)", updatable = false, nullable = false)
    private String id;

    @Column(name = "customerId", columnDefinition = "VARCHAR(36)", nullable = false)
    private String customerId;

    @Column(name = "productId", columnDefinition = "VARCHAR(36)", nullable = false)
    private String productId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "purchaseDate", nullable = false)
    private Date purchaseDate;
}
