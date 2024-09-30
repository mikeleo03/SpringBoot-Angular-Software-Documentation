package com.example.authentication.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.authentication.data.model.ApiKey;

@Repository
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long> {
    
    // Get the first API key, order by ID 
    Optional<ApiKey> findFirstByOrderById();

    // Find the first active API key
    Optional<ApiKey> findFirstByActiveTrueOrderById(); 
}