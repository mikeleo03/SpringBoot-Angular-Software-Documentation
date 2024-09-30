package com.example.lecture_13.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lecture_13.data.model.APIKey;

@Repository
public interface APIKeyRepository extends JpaRepository<APIKey, Long> {
    
    // Get the first API key, order by ID 
    Optional<APIKey> findFirstByOrderById();

    // Find the first active API key
    Optional<APIKey> findFirstByActiveTrueOrderById(); 
}