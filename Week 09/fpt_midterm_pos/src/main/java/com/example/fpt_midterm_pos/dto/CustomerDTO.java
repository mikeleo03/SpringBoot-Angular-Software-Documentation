package com.example.fpt_midterm_pos.dto;

import com.example.fpt_midterm_pos.data.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private UUID id;
    private String name;
    private String phoneNumber;
    private Status status;
}
