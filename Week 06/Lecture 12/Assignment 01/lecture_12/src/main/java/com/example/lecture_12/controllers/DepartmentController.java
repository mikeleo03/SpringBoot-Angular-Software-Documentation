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

import com.example.lecture_12.data.model.Department;
import com.example.lecture_12.services.DepartmentService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/departments")
@AllArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * This method retrieves {@link Page} of {@link Department} from the database.
     *
     * @param page The page number to retrieve (0-based index).
     * @param size The number of elements per page.
     * @return ResponseEntity<List<Department>> - A response entity containing a pages of {@link Department}.
     * If the pages is empty, it returns a HTTP status code 204 (No Content).
     * If the operation is successful, it returns a HTTP status code 200 (OK) with the pages of {@link Department}.
     */
    @GetMapping
    public ResponseEntity<Page<Department>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Department> departments = departmentService.findAll(pageable);

        if (departments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(departments);
    }

     /**
     * This method retrieves an {@link Department} from the database by its deptNo.
     *
     * @param deptNo The unique identifier of the {@link Department}.
     * @return ResponseEntity<Department> - A response entity containing the {@link Department} if found, or a 404 Not Found status code if not found.
     */
    @GetMapping(value = "/{deptNo}")
    public ResponseEntity<Department> findDepartmentById(@PathVariable("deptNo") String deptNo) {
        Optional<Department> departmentOpt= departmentService.findById(deptNo);

        if(departmentOpt.isPresent()) {
            return ResponseEntity.ok(departmentOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * This method saves a {@link Department} to the database.
     *
     * @param department The department object to be saved.
     * @return ResponseEntity<Department> - A response entity containing the saved {@link Department}.
     * If the {@link Department} already exists in the database, it returns a HTTP status code 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Department> save(@RequestBody Department department) {
        Optional<Department> departmentOpt = departmentService.findById(department.getDeptNo());
        
        if (departmentOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(departmentService.save(department));
    }

    /**
     * This method updates an existing {@link Department} in the database.
     *
     * @param department The department object to be updated.
     * @return ResponseEntity<Department> - A response entity containing the updated {@link Department}.
     * If the {@link Department} does not exist in the database, it returns a HTTP status code 404 (Not Found).
     */
    @PutMapping(value = "/{deptNo}")
    public ResponseEntity<Department> update(@PathVariable("deptNo") String deptNo, @RequestBody Department department) {
        Optional<Department> departmentOpt = departmentService.findById(deptNo);
        
        if (departmentOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(departmentService.save(department));
    }

    /**
     * This method deletes an {@link Department} from the database by its deptNo.
     *
     * @param deptNo The unique identifier of the {@link Department} to be deleted.
     * @return ResponseEntity<Department> - A response entity containing the deleted {@link Department} if found, or a 404 Not Found status code if not found.
     */
    @DeleteMapping(value = "/{deptNo}")
    public ResponseEntity<Department> deleteDepartment(@PathVariable(value = "deptNo") String deptNo) {
        Optional<Department> departmentOpt = departmentService.findById(deptNo);

        if(departmentOpt.isPresent()) {
            departmentService.deleteById(deptNo);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
