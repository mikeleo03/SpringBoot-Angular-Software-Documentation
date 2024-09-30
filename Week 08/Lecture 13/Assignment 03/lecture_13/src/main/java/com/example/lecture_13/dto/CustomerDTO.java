package com.example.lecture_13.dto;

import com.example.lecture_13.data.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private UUID Id;
    private String name;
    private String phoneNumber;
    private Status status;
}
