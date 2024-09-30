package com.example.spring_oauth2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
