package com.example.lecture_12.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

import com.example.lecture_12.data.model.Employee;
import com.example.lecture_12.dto.EmployeeSearchCriteriaDTO;
import com.example.lecture_12.services.EmployeeService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * This method retrieves {@link Page} of {@link Employee} from the database.
     *
     * @param page The page number to retrieve (0-based index).
     * @param size The number of elements per page.
     * @return ResponseEntity<Page<Employee>> - A response entity containing a page of {@link Employee}.
     * If the page is empty, it returns a HTTP status code 204 (No Content).
     * If the operation is successful, it returns a HTTP status code 200 (OK) with the page of {@link Employee}.
     */
    @GetMapping
    public ResponseEntity<Page<Employee>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.findAll(pageable);

        if (employees.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(employees);
    }

    /**
     * Endpoint to search for {@link Employee} entities based on the provided search criteria.
     * Supports pagination and sorting.
     *
     * @param criteria The criteria object of {@link EmployeeSearchCriteriaDTO} containing fields to filter the search.
     * @param page     The page number to retrieve (default is 0).
     * @param size     The number of elements per page (default is 20).
     * @return ResponseEntity containing a {@link Page} of {@link Employee} entities that match the criteria,  
     */
    @GetMapping("/search")
    public ResponseEntity<Page<Employee>> searchEmployees(EmployeeSearchCriteriaDTO criteria, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.findByCriteria(criteria, pageable);

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
     */
    @PostMapping
    public ResponseEntity<Employee> save(@RequestBody Employee employee) {
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
    public ResponseEntity<Employee> update(@PathVariable Integer empNo, @RequestBody Employee employee) {
        Optional<Employee> employeeOpt = employeeService.findById(empNo);
        
        if (employeeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        employee.setEmpNo(empNo);
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
