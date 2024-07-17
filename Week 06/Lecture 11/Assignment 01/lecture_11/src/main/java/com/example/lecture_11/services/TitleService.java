package com.example.lecture_11.services;

import java.util.Optional;

import com.example.lecture_11.data.model.Title;
import com.example.lecture_11.data.model.composite.TitleId;

public interface TitleService {
    // Retrieves an {@link Title} entity by its unique identifier.
    Optional<Title> findById(TitleId id);

    // Saves or updates an {@link Title} entity in the database.
    Title saveOrUpdate(Title title);

    // Deletes an {@link Title} entity from the database by its unique identifier.
    void deleteById(TitleId id);
}
