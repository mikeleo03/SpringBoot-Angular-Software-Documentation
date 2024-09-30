package com.example.lecture_8_2.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.lecture_8_2.model.Employee;

@Repository
public class EmployeeRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // RowMapper to map ResultSet to Employee
    private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Employee employee = new Employee();
        employee.setId(rs.getString("id"));
        employee.setName(rs.getString("name"));
        employee.setDob(rs.getDate("dob").toLocalDate());
        employee.setAddress(rs.getString("address"));
        employee.setDepartment(rs.getString("department"));
        return employee;
    };

    // Find all employees
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate.query(sql, EMPLOYEE_ROW_MAPPER);
    }

    // Find employee by ID
    public Optional<Employee> findById(String id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try {
            Employee employee = jdbcTemplate.queryForObject(sql, EMPLOYEE_ROW_MAPPER, id);
            return Optional.ofNullable(employee);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    // Save new employee
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employee (id, name, dob, address, department) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getId(), employee.getName(), employee.getDob(),
                employee.getAddress(), employee.getDepartment());
        return employee;
    }

    // Update existing employee
    public Employee update(Employee employee) {
        String sql = "UPDATE employee SET name = ?, dob = ?, address = ?, department = ? WHERE id = ?";
        jdbcTemplate.update(sql, employee.getName(), employee.getDob(),
                employee.getAddress(), employee.getDepartment(), employee.getId());
        return employee;
    }

    // Delete employee by ID
    public void deleteById(String id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Find employees by department
    public List<Employee> findByDepartmentId(String department) {
        String sql = "SELECT * FROM employee WHERE department = ?";
        return jdbcTemplate.query(sql, EMPLOYEE_ROW_MAPPER, department);
    }
}