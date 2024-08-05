package com.example.fpt_midterm_pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveDTO {
    @Schema(example = "Your Name")
    private String name;

    @Schema(example = "+62xxxxxxxxxxxxx")
    private String phoneNumber;
}
