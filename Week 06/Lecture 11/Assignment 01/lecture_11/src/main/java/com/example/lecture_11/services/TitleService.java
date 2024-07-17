package com.example.lecture_11.services;

import com.example.lecture_11.data.model.Title;
import com.example.lecture_11.data.model.composite.TitleId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TitleService {
    // Retrieves a paginated list of {@link Title} entities.
    Page<Title> findAll(Pageable pageable);

    // Retrieves an {@link Title} entity by its unique identifier.
    Optional<Title> findById(TitleId id);

    // Saves or updates an {@link Title} entity in the database.
    Title saveOrUpdate(Title title);

    // Deletes an {@link Title} entity from the database by its unique identifier.
    void deleteById(TitleId id);
}
