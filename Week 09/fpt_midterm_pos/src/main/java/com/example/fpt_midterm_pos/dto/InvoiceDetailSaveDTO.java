package com.example.fpt_midterm_pos.dto;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDetailSaveDTO {

    @NotNull(message = "productID can't be NULL")
    private UUID productId;

    @NotNull(message = "Quantity can't be NULL")
    @Min(value = 0, message = "Quantity must be nonnegative")
    private Integer quantity;
}
