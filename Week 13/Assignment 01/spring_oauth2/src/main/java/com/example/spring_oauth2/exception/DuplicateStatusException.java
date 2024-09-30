package com.example.spring_oauth2.exception;

public class DuplicateStatusException extends RuntimeException {
    public DuplicateStatusException(String message) {
        super(message);
    }
}
