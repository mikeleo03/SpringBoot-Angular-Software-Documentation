package com.example.lecture_11.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lecture_11.data.model.Salary;
import com.example.lecture_11.data.model.composite.SalaryId;

public interface SalaryRepository extends JpaRepository<Salary, SalaryId> {
}
