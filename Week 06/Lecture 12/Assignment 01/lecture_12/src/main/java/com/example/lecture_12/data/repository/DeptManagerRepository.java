package com.example.lecture_12.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lecture_12.data.model.DeptManager;
import com.example.lecture_12.data.model.composite.DeptManagerId;

public interface DeptManagerRepository extends JpaRepository<DeptManager, DeptManagerId> {
}

