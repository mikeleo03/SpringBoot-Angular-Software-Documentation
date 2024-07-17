package com.example.lecture_11.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecture_11.data.model.Salary;
import com.example.lecture_11.data.model.composite.SalaryId;
import com.example.lecture_11.services.SalaryService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/salaries")
@AllArgsConstructor
public class SalaryController {

    private final SalaryService salaryService;

    /**
     * This method retrieves a {@link Salary} from the database by its unique identifier.
     *
     * @param id The unique identifier of the {@link Salary} to be retrieved.
     * @return ResponseEntity<Salary> - A response entity containing the {@link Salary} if found, or a 404 Not Found status code if not found.
     */
    @GetMapping
    public ResponseEntity<Salary> findSalaryById(@RequestBody SalaryId id) {
        Optional<Salary> salaryOpt= salaryService.findById(id);

        if(salaryOpt.isPresent()) {
            return ResponseEntity.ok(salaryOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * This method deletes a {@link Salary} from the database by its unique identifier.
     *
     * @param id The unique identifier of the {@link Salary} to be deleted.
     * @return ResponseEntity<Salary> - A response entity containing the deleted {@link Salary} if found and successfully deleted, or a 404 Not Found status code if not found.
     */
    @PostMapping
    public ResponseEntity<Salary> saveOrUpdate(@RequestBody Salary salary) {
        Optional<Salary> salaryOpt = salaryService.findById(salary.getId());
        
        if(salaryOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(salaryService.saveOrUpdate(salary));
    }

    /**
     * This method deletes an {@link Salary} from the database by its id.
     *
     * @param id The unique identifier of the {@link Salary} to be deleted.
     * @return ResponseEntity<Salary> - A response entity containing the deleted {@link Salary} if found, or a 404 Not Found status code if not found.
     */
    @DeleteMapping
    public ResponseEntity<Salary> deleteSalary(@RequestBody SalaryId id) {
        Optional<Salary> salaryOpt = salaryService.findById(id);

        if(salaryOpt.isPresent()) {
            salaryService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
