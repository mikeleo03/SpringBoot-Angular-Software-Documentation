package com.example.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveDTO {
    private String firstName;
    private String lastName;
    private String email;
}
