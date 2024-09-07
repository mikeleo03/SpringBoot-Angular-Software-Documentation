package com.example.spring_oauth2.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
