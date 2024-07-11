package com.example.lecture_10.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

import com.example.lecture_10.data.model.Employee;
import com.example.lecture_10.data.repository.EmployeeRepository;
import com.example.lecture_10.dto.EmployeeDTO;
import com.example.lecture_10.mapper.EmployeeMapper;
import com.example.lecture_10.util.FileUtils;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/employee")
@AllArgsConstructor
@Validated
public class EmployeeController {

    @Autowired
    private final EmployeeRepository employeeRepository;

    private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

    /**
     * Retrieves a list of all employees, optionally filtered by department.
     *
     * @param departmentId The ID of the department to filter employees by. If not provided, all employees are returned.
     * @return A ResponseEntity containing a list of EmployeeDTOs representing the retrieved employees.
     *         If no employees are found, an empty ResponseEntity with status 204 (No Content) is returned.
     */
    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> listAllEmployee(@RequestParam(value = "department", required = false) String departmentId) {
        List<Employee> employees;

        if (departmentId != null && !departmentId.isEmpty()) {
            employees = employeeRepository.findByDepartmentId(departmentId);
        } else {
            employees = employeeRepository.findAll();
        }

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<EmployeeDTO> employeeDTOs = employees.stream()
                                                  .map(employeeMapper::toEmployeeDTO)
                                                  .collect(Collectors.toList());

        return ResponseEntity.ok(employeeDTOs);
    }

    /**
     * Retrieves an employee by their unique ID.
     *
     * @param id The unique identifier of the employee to retrieve.
     * @return A ResponseEntity containing an EmployeeDTO representing the retrieved employee.
     *         If the employee is not found, a ResponseEntity with status 404 (Not Found) is returned.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> findEmployeeById(@PathVariable("id") String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);

        if (employeeOpt.isPresent()) {
            EmployeeDTO employeeDTO = employeeMapper.toEmployeeDTO(employeeOpt.get());
            return ResponseEntity.ok(employeeDTO);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Saves a new employee to the database and returns the saved employee as an EmployeeDTO.
     *
     * @param employeeDTO The EmployeeDTO containing the details of the new employee to be saved.
     * @return A ResponseEntity containing the saved employee as an EmployeeDTO.
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = employeeMapper.toEmployee(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(employeeMapper.toEmployeeDTO(savedEmployee));
    }

    /**
     * Updates an existing employee in the database with the provided EmployeeDTO.
     *
     * @param id The unique identifier of the employee to be updated.
     * @param employeeDTO The EmployeeDTO containing the updated details of the employee.
     * @return A ResponseEntity containing the updated employee as an EmployeeDTO if the employee is found, otherwise a ResponseEntity with status 404 (Not Found).
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable(value = "id") String id, @RequestBody EmployeeDTO employeeDTO) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);

        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.setName(employeeDTO.getName());
            employee.setDob(employeeDTO.getDob());
            employee.setAddress(employeeDTO.getAddress());
            employee.setDepartment(employeeDTO.getDepartment());
            employee.setEmail(employeeDTO.getEmail());
            employee.setPhone(employeeDTO.getPhone());

            Employee updatedEmployee = employeeRepository.save(employee);
            return ResponseEntity.ok(employeeMapper.toEmployeeDTO(updatedEmployee));
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Deletes an existing employee from the database based on the provided unique ID.
     *
     * @param id The unique identifier of the employee to be deleted.
     * @return A ResponseEntity containing the deleted employee if found, otherwise a ResponseEntity with status 404 (Not Found).
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable(value = "id") String id) {
        Optional<Employee> employeeOpt = employeeRepository.findById(id);

        if (employeeOpt.isPresent()) {
            employeeRepository.delete(employeeOpt.get());
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Uploads a CSV file containing employee data and saves it to the database.
     *
     * @param file The MultipartFile containing the CSV file to be uploaded.
     * @return A ResponseEntity containing a success message if the file is read successfully and data is saved, otherwise a ResponseEntity with a bad request status and an error message.
     * @throws IOException If an error occurs while reading the CSV file.
     */
    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsv(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        List<EmployeeDTO> employeeDTOs;
        try {
            employeeDTOs = FileUtils.readEmployeesFromCSV(file).stream()
                    .map(employeeMapper::toEmployeeDTO)
                    .collect(Collectors.toList());

            List<Employee> employees = employeeDTOs.stream()
                    .map(employeeMapper::toEmployee)
                    .collect(Collectors.toList());

            employeeRepository.saveAll(employees);
            return ResponseEntity.ok("File read successfully and data saved.");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

