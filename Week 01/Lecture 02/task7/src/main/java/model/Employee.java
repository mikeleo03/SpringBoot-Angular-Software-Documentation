package model;

import java.time.LocalDate;

import utils.DateUtils;

public class Employee {
    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private String address;
    private String department;

    public Employee(String id, String name, LocalDate dateOfBirth, String address, String department) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.department = department;
    }

    /**
     * Converts the Employee object to a CSV string format.
     *
     * @return a CSV string representation of the Employee object.
     */
    public String toCSV() {
        return String.join(",",
                id,
                name,
                DateUtils.formatDate(dateOfBirth),
                address,
                department);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public String getDepartment() {
        return department;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void getName(String name) {
        this.name = name;
    }

    public void getDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void getAddress(String address) {
        this.address = address;
    }

    public void getDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return id + "," + name + "," + dateOfBirth + "," + address + "," + department;
    }
}