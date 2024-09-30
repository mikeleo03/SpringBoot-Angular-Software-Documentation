package com.example.lecture_8_2.repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.lecture_8_2.model.Employee;

@Repository
public class EmployeeRepository {

    @Autowired
    @Qualifier("jdbcTemplate1")
    private JdbcTemplate jdbcTemplate1;

    @Autowired
    @Qualifier("jdbcTemplate2")
    private JdbcTemplate jdbcTemplate2;

    private static final RowMapper<Employee> EMPLOYEE_ROW_MAPPER = (ResultSet rs, int rowNum) -> {
        Employee employee = new Employee();
        employee.setId(rs.getString("id"));
        employee.setName(rs.getString("name"));
        employee.setDob(rs.getDate("dob").toLocalDate());
        employee.setAddress(rs.getString("address"));
        employee.setDepartment(rs.getString("department"));
        return employee;
    };

    public List<Employee> findAllFromDS1() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate1.query(sql, EMPLOYEE_ROW_MAPPER);
    }

    public List<Employee> findAllFromDS2() {
        String sql = "SELECT * FROM employee";
        return jdbcTemplate2.query(sql, EMPLOYEE_ROW_MAPPER);
    }

    public Optional<Employee> findByIdFromDS1(String id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try {
            Employee employee = jdbcTemplate1.queryForObject(sql, EMPLOYEE_ROW_MAPPER, id);
            return Optional.ofNullable(employee);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Employee> findByIdFromDS2(String id) {
        String sql = "SELECT * FROM employee WHERE id = ?";
        try {
            Employee employee = jdbcTemplate2.queryForObject(sql, EMPLOYEE_ROW_MAPPER, id);
            return Optional.ofNullable(employee);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void saveToDS1(Employee employee) {
        String sql = "INSERT INTO employee (id, name, dob, address, department) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate1.update(sql, employee.getId(), employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment());
    }

    public void saveToDS2(Employee employee) {
        String sql = "INSERT INTO employee (id, name, dob, address, department) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate2.update(sql, employee.getId(), employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment());
    }

    public void saveToDS2Fail(Employee employee) {
        String sql = "INSERT INTO employee (id, name, dateOfBirth, address, department) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate2.update(sql, employee.getId(), employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment());
    }

    public void updateInDS1(Employee employee) {
        String sql = "UPDATE employee SET name = ?, dob = ?, address = ?, department = ? WHERE id = ?";
        jdbcTemplate1.update(sql, employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment(), employee.getId());
    }

    public void updateInDS2(Employee employee) {
        String sql = "UPDATE employee SET name = ?, dob = ?, address = ?, department = ? WHERE id = ?";
        jdbcTemplate2.update(sql, employee.getName(), employee.getDob(), employee.getAddress(), employee.getDepartment(), employee.getId());
    }

    public void deleteFromDS1ById(String id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        jdbcTemplate1.update(sql, id);
    }

    public void deleteFromDS2ById(String id) {
        String sql = "DELETE FROM employee WHERE id = ?";
        jdbcTemplate2.update(sql, id);
    }
}