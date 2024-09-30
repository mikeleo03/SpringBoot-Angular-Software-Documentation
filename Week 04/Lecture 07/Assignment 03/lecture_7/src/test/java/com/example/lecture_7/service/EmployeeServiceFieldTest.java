package com.example.lecture_7.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;

public class EmployeeServiceFieldTest {

    @InjectMocks
    private EmployeeServiceField employeeService;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Custom behavior for emailService.sendEmail
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            System.out.println("Sending email to " + args[0]);
            System.out.println("Subject: " + args[1]);
            System.out.println("Body: " + args[2]);
            return null;
        }).when(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    public void testNotifyEmployee() {
        String email = "employee@example.com";
        String subject = "Subject";
        String body = "Body";

        employeeService.notifyEmployee(email, subject, body);

        // Output is printed by the custom behavior
    }
}