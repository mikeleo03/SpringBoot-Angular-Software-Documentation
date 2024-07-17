package com.example.lecture_11.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lecture_11.data.model.DeptEmp;
import com.example.lecture_11.data.model.composite.DeptEmpId;

public interface DeptEmpRepository extends JpaRepository<DeptEmp, DeptEmpId> {
}

