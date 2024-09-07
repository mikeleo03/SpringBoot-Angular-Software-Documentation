package com.example.spring_oauth2.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_oauth2.service.TokenService;
import com.example.spring_oauth2.data.model.User;
import com.example.spring_oauth2.dto.JwtRequest;
import com.example.spring_oauth2.dto.JwtResponse;
import com.example.spring_oauth2.service.AuthService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthService authService;

    @Autowired
    public AuthenticationController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authRequest) {
        // Authenticate the user first
        authService.authenticate(authRequest.getUsername(), authRequest.getPassword());

        // Generate JWT token
        final String jwt = tokenService.generateToken(authRequest.getUsername());

        // Return the token as a response
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody JwtRequest request) {
        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        authService.saveUser(newUser);
        return ResponseEntity.status(HttpStatus.OK).body("User registered successfully");
    }
}

