package com.example.lecture_10.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.lecture_10.model.Employee;
import com.example.lecture_10.repository.EmployeeRepository;
import com.example.lecture_10.util.FileUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
public class EmployeeController {

    @Autowired
    private final EmployeeRepository employeeRepository;

    /**
     * This method retrieves employees from the database.
     * If a department query parameter is provided, it filters employees by department.
     *
     * @param department Optional query parameter to filter employees by department.
     * @return ResponseEntity<List<Employee>> - A response entity containing a list of employees.
     * If the list is empty, it returns a HTTP status code 204 (No Content).
     * If the operation is successful, it returns a HTTP status code 200 (OK) with the list of employees.
     */
    @GetMapping
    public ResponseEntity<List<Employee>> listAllEmployee(@RequestParam(value = "department", required = false) String departmentId) {
        List<Employee> employees;

        if (departmentId != null && !departmentId.isEmpty()) {
            employees = employeeRepository.findByDepartmentId(departmentId);
        } else {
            employees = employeeRepository.findAll();
        }

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(employees);
    }

    /**
     * This method retrieves an employee from the database by its id.
     *
     * @param id The unique identifier of the employee.
     * @return ResponseEntity<Employee> - A response entity containing the employee if found, or a 404 Not Found status code if not found.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("id") String id) {
        Optional<Employee> employeeOpt= employeeRepository.findById(id);
        if(employeeOpt.isPresent()) {
            return ResponseEntity.ok(employeeOpt.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This method saves an employee to the database.
     *
     * @param employee The employee object to be saved.
     * @return ResponseEntity<Employee> - A response entity containing the saved employee.
     * If the employee already exists in the database, it returns a HTTP status code 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) {
        Optional<Employee> employeeOpt = employeeRepository.findById(employee.getId());
        if(employeeOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    /**
     * This method updates an employee in the database by its id.
     *
     * @param id The unique identifier of the employee.
     * @param employeeForm The updated employee information.
     * @return ResponseEntity<Employee> - A response entity containing the updated employee if found, or a 404 Not Found status code if not found.
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") String id,
                                                 @RequestBody Employee employeeForm) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if(employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.setName(employeeForm.getName());
            employee.setDob(employeeForm.getDob());
            employee.setAddress(employeeForm.getAddress());
            employee.setDepartment(employeeForm.getDepartment());

            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(updatedEmployee);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This method deletes an employee from the database by its id.
     *
     * @param id The unique identifier of the employee to be deleted.
     * @return ResponseEntity<Employee> - A response entity containing the deleted employee if found, or a 404 Not Found status code if not found.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable(value = "id") String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);
        if(employeeOpt.isPresent()) {
            employeeRepository.delete(employeeOpt.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This method receives an input CSV file, reads the data, and saves it to the database.
     *
     * @param file The CSV file containing employee data.
     * @return ResponseEntity<String> - A response entity indicating the success or failure of the operation.
     */
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        List<Employee> employees;
        try {
            employees = FileUtils.readEmployeesFromCSV(file);
            employeeRepository.saveAll(employees);
            return ResponseEntity.ok("File readed successfully and data saved.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

