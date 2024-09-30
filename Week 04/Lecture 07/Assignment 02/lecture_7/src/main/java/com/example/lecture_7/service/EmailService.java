package com.example.lecture_7.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}