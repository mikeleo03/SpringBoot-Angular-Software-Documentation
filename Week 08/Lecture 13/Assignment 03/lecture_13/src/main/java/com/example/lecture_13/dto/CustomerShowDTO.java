package com.example.lecture_13.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerShowDTO {
    private UUID Id;
    private String name;
    private String phoneNumber;
}
