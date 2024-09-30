package com.example.lecture_12.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lecture_12.data.model.Salary;
import com.example.lecture_12.data.model.composite.SalaryId;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
}
