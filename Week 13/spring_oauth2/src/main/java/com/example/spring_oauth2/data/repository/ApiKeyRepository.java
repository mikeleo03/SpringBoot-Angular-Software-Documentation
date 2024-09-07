package com.example.spring_oauth2.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_oauth2.data.model.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    
    // Get the first API key, order by ID 
    Optional<ApiKey> findFirstByOrderById();

    // Find the first active API key
    Optional<ApiKey> findFirstByActiveTrueOrderById();

    // Find the first active API key with a specific API key
    Optional<ApiKey> findFirstByApiKeyAndActiveTrue(String apiKey); 
}
