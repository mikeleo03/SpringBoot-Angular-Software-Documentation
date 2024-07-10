package com.example.lecture_9_2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.lecture_9_2.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Get all employees from the repository and order them by name ascending
    List<Employee> findAllByOrderByNameAsc();

    // Retrieves a paginated list of all employees from the database, sorted by their names in ascending order.
    Page<Employee> findAllByOrderByNameAsc(Pageable pageable);

    @Query(value = "SELECT MAX(salary) FROM employee", nativeQuery = true)
    Optional<Integer> findMaxSalary();

    @Query(value = "SELECT MIN(salary) FROM employee", nativeQuery = true)
    Optional<Integer> findMinSalary();

    @Query(value = "SELECT AVG(salary) FROM employee", nativeQuery = true)
    Double findAverageSalary();

    @Query(value = "SELECT e.name " +
                "FROM employee e " +
                "WHERE e.salary = (SELECT MAX(salary) FROM employee)", nativeQuery = true)
    List<String> findEmployeeNamesWithHighestSalary();

    @Query(value = "SELECT e.name " +
                "FROM employee e " +
                "WHERE e.salary = (SELECT MIN(salary) FROM employee)", nativeQuery = true)
    List<String> findEmployeeNamesWithLowestSalary();
}