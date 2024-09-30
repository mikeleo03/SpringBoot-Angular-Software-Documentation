package com.example.fpt_midterm_pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerShowDTO {
    private UUID id;
    private String name;
    private String phoneNumber;
}