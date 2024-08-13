package com.example.authentication.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.authentication.data.repository.ApiKeyRepository;
import com.example.authentication.data.model.ApiKey;
import com.example.authentication.service.ApiKeyService;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    @Autowired
    public ApiKeyServiceImpl(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    /**
     * Validates if the provided API key is valid and active.
     *
     * @param requestApiKey The API key to be validated.
     * @return {@code true} if the provided API key is valid and active, {@code false} otherwise.
     */
    @Override
    public boolean isValidApiKey(String requestApiKey) {
        // Check from the repo
        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
        if (apiKeyOpt.isPresent()) {
            // Check if it's the same
            String storedApiKey = apiKeyOpt.get().getApiKey();
            return storedApiKey.equals(requestApiKey);
        }
        return false;
    }
}
