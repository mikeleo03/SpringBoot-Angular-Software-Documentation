package com.example.fpt_midterm_pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSaveDTO {
    
    @NotBlank(message = "Name is mandatory")
    @NotNull(message = "Name can't be NULL")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    @Schema(example = "Your Name")
    private String name;
    
    @NotBlank(message = "Phone is mandatory")
    @NotNull(message = "Phone can't be NULL")
    @Pattern(regexp = "^\\+62\\d{9,13}$", message = "Phone number must start with +62 and contain 9 to 13 digits")
    @Schema(example = "+62xxxxxxxxxxxxx")
    private String phoneNumber;
}
