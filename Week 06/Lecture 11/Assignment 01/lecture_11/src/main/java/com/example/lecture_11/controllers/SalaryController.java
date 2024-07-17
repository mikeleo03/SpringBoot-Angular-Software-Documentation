package com.example.lecture_11.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * This method retrieves {@link Page} of {@link Salary} from the database.
     *
     * @return ResponseEntity<List<Salary>> - A response entity containing a pages of {@link Salary}.
     * If the pages is empty, it returns a HTTP status code 204 (No Content).
     * If the operation is successful, it returns a HTTP status code 200 (OK) with the pages of {@link Salary}.
     */
    @GetMapping
    public ResponseEntity<Page<Salary>> findAll(Pageable pageable) {
        Page<Salary> salaries = salaryService.findAll(pageable);

        if (salaries.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(salaries);
    }

     /**
     * This method retrieves an {@link Salary} from the database by its id.
     *
     * @param id The unique identifier of the {@link Salary}.
     * @return ResponseEntity<Salary> - A response entity containing the {@link Salary} if found, or a 404 Not Found status code if not found.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Salary> findSalaryById(@PathVariable("id") SalaryId id) {
        Optional<Salary> salaryOpt= salaryService.findById(id);

        if(salaryOpt.isPresent()) {
            return ResponseEntity.ok(salaryOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * This method saves or updates a {@link Salary} to the database.
     *
     * @param salary The salary object to be saved.
     * @return ResponseEntity<Salary> - A response entity containing the saved {@link Salary}.
     * If the {@link Salary} already exists in the database, it returns a HTTP status code 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Salary> saveOrUpdate(@RequestBody Salary salary) {
        SalaryId salaryId = new SalaryId(salary.getEmpNo(), salary.getFromDate());
        Optional<Salary> salaryOpt = salaryService.findById(salaryId);
        
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
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Salary> deleteSalary(@PathVariable(value = "id") SalaryId id) {
        Optional<Salary> salaryOpt = salaryService.findById(id);

        if(salaryOpt.isPresent()) {
            salaryService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
