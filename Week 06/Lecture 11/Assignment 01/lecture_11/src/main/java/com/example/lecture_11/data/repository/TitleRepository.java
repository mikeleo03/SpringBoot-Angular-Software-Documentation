package com.example.lecture_11.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.lecture_11.data.model.Title;
import com.example.lecture_11.data.model.composite.TitleId;

public interface TitleRepository extends JpaRepository<Title, TitleId> {
}
