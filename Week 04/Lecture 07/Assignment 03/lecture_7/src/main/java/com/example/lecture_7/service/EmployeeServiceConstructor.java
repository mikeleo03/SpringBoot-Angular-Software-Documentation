package com.example.lecture_7.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class EmployeeServiceConstructor {
    private final EmailService emailService;

    @Autowired
    public EmployeeServiceConstructor(EmailService emailService) {
        this.emailService = emailService;
    }

    public void notifyEmployee(String email, String subject, String body) {
        emailService.sendEmail(email, subject, body);
        System.out.println("EmployeeServiceConstructor instance hash: " + this.hashCode());
    }
}
