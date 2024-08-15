package com.example.authentication.service;

public interface ApiKeyService {

    // Validates if the provided API key is valid and active
    boolean isValidApiKey(String requestApiKey);
}

