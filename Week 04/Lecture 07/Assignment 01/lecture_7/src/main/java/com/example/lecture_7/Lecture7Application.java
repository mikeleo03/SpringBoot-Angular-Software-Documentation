package com.example.lecture_7;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.example.lecture_7.config.AppConfig;
import com.example.lecture_7.entity.Employee;

public class Lecture7Application {
	public static void main(String[] args) {
        @SuppressWarnings("resource")
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Employee employee = context.getBean(Employee.class);
        employee.working();
    }
}
