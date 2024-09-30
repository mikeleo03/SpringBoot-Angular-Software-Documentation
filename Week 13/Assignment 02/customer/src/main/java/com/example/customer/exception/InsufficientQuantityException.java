package com.example.customer.exception;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String message) {
        super(message);
    }
}

