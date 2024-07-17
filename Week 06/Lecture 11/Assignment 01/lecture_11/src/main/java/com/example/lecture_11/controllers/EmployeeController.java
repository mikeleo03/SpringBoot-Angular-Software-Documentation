package com.example.lecture_11.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecture_11.data.model.Employee;
import com.example.lecture_11.services.EmployeeService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * This method retrieves {@link Page} of {@link Employee} from the database.
     *
     * @return ResponseEntity<List<Employee>> - A response entity containing a pages of {@link Employee}.
     * If the pages is empty, it returns a HTTP status code 204 (No Content).
     * If the operation is successful, it returns a HTTP status code 200 (OK) with the pages of {@link Employee}.
     */
    @GetMapping
    public ResponseEntity<Page<Employee>> findAll(Pageable pageable) {
        Page<Employee> employees = employeeService.findAll(pageable);

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(employees);
    }

     /**
     * This method retrieves an {@link Employee} from the database by its empNo.
     *
     * @param empNo The unique identifier of the {@link Employee}.
     * @return ResponseEntity<Employee> - A response entity containing the {@link Employee} if found, or a 404 Not Found status code if not found.
     */
    @GetMapping(value = "/{empNo}")
    public ResponseEntity<Employee> findEmployeeById(@PathVariable("empNo") Integer empNo) {
        Optional<Employee> employeeOpt= employeeService.findById(empNo);

        if(employeeOpt.isPresent()) {
            return ResponseEntity.ok(employeeOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * This method saves an {@link Employee} to the database.
     *
     * @param employee The employee object to be saved.
     * @return ResponseEntity<Employee> - A response entity containing the saved {@link Employee}.
     * If the {@link Employee} already exists in the database, it returns a HTTP status code 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody Employee employee) {
        Optional<Employee> employeeOpt = employeeService.findById(employee.getEmpNo());
        
        if (employeeOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(employeeService.save(employee));
    }

    /**
     * This method updates an existing {@link Employee} in the database.
     *
     * @param empNo The unique identifier of the {@link Employee} to be updated.
     * @param employee The employee object to be updated.
     * @return ResponseEntity<Employee> - A response entity containing the updated {@link Employee}.
     * If the {@link Employee} does not exist in the database, it returns a HTTP status code 404 (Not Found).
     */
    @PutMapping(value = "/{empNo}")
    public ResponseEntity<Employee> update(@PathVariable(value = "/{empNo}") Integer empNo, @RequestBody Employee employee) {
        Optional<Employee> employeeOpt = employeeService.findById(empNo);
        
        if (employeeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(employeeService.save(employee));
    }

    /**
     * This method deletes an {@link Employee} from the database by its empNo.
     *
     * @param empNo The unique identifier of the {@link Employee} to be deleted.
     * @return ResponseEntity<Employee> - A response entity containing the deleted {@link Employee} if found, or a 404 Not Found status code if not found.
     */
    @DeleteMapping(value = "/{empNo}")
    public ResponseEntity<Employee> deleteEmployee(@PathVariable(value = "empNo") Integer empNo) {
        Optional<Employee> employeeOpt = employeeService.findById(empNo);

        if(employeeOpt.isPresent()) {
            employeeService.deleteById(empNo);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
