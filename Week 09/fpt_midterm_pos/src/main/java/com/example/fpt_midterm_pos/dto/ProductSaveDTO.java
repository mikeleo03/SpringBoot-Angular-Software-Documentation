package com.example.fpt_midterm_pos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSaveDTO {

    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name can't be NULL")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    private String name;
    
    @NotNull(message = "Price can't be NULL")
    @Min(value = 0, message = "Price must be nonnegative")
    private Double price;

    @NotNull(message = "Quantity can't be NULL")
    @Min(value = 0, message = "Quantity must be nonnegative")
    private Integer quantity;
}
