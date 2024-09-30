package com.example.lecture_7;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.example.lecture_7.service.EmailService;
import com.example.lecture_7.service.EmployeeServiceConstructor;

@SpringBootApplication
public class Lecture7Application implements CommandLineRunner {

    private final ApplicationContext context;

    public Lecture7Application(ApplicationContext context) {
        this.context = context;
    }

	public static void main(String[] args) {
		SpringApplication.run(Lecture7Application.class, args);
	}

	@Override
    public void run(String... args) throws Exception {
		System.out.println("Testing Singleton Scope for EmailServiceImpl:");
        EmailService emailService1 = context.getBean(EmailService.class);
        EmailService emailService2 = context.getBean(EmailService.class);

        emailService1.sendEmail("singleton@example.com", "Singleton Test", "Testing Singleton Scope");
        emailService2.sendEmail("singleton@example.com", "Singleton Test", "Testing Singleton Scope");

        System.out.println("\nTesting Prototype Scope for EmployeeServiceConstructor:");
        EmployeeServiceConstructor employeeServiceConstructor1 = context.getBean(EmployeeServiceConstructor.class);
        EmployeeServiceConstructor employeeServiceConstructor2 = context.getBean(EmployeeServiceConstructor.class);

        employeeServiceConstructor1.notifyEmployee("employee1@example.com", "Prototype Test 1", "Testing Prototype Scope 1");
        employeeServiceConstructor2.notifyEmployee("employee2@example.com", "Prototype Test 2", "Testing Prototype Scope 2");
    }
}

