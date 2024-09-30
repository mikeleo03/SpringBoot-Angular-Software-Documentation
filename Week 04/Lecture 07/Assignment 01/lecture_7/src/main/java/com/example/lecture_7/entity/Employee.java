package com.example.lecture_7.entity;
import com.example.lecture_7.EmployeeWork;

public class Employee {
    private String id;
    private String name;
    private int age;
    private EmployeeWork employeeWork;

    // No-argument constructor
    public Employee() {}

    // Constructor for DI
    public Employee(String id, String name, int age, EmployeeWork employeeWork) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.employeeWork = employeeWork;
    }

    public void working() {
        System.out.println("Employee ID: " + id);
        System.out.println("Employee Name: " + name);
        System.out.println("Employee Age: " + age);
        employeeWork.work();
    }

    // Getters and setters (optional, depending on your needs)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public EmployeeWork getEmployeeWork() {
        return employeeWork;
    }

    // Setter for ID
    public void setId(String id) {
        this.id = id;
    }

    // Setter for name
    public void setName(String name) {
        this.name = name;
    }

    // Setter for age
    public void setAge(int age) {
        this.age = age;
    }

    // Setter for EmployeeWork
    public void setEmployeeWork(EmployeeWork employeeWork) {
        this.employeeWork = employeeWork;
    }
}
