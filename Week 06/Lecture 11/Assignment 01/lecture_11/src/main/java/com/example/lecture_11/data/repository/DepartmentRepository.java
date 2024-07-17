package com.example.lecture_11.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.lecture_11.data.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, String> {
}

