package com.example.lecture_7.service;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class EmailServiceImpl implements EmailService {
    @Override
    public void sendEmail(String to, String subject, String body) {
        // Simulate email sending (in real scenarios, integrate with email server)
        System.out.println("Sending email to " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("EmailServiceImpl instance hash: " + this.hashCode());
    }
}
