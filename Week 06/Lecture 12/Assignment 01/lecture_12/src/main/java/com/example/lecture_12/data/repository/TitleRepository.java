package com.example.lecture_12.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lecture_12.data.model.Title;
import com.example.lecture_12.data.model.composite.TitleId;

public interface TitleRepository extends JpaRepository<Title, TitleId> {
}
