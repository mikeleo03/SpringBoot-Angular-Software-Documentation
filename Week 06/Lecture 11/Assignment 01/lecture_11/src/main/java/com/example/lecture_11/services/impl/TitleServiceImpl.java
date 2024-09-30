package com.example.lecture_11.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.lecture_11.data.model.Title;
import com.example.lecture_11.data.model.composite.TitleId;
import com.example.lecture_11.data.repository.TitleRepository;
import com.example.lecture_11.services.TitleService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TitleServiceImpl implements TitleService {
    
    private final TitleRepository titleRepository;

    /**
     * Retrieves an {@link Title} entity by its unique identifier.
     *
     * @param id The unique identifier of the {@link Title} entity to retrieve.
     * @return An {@link Optional} containing the {@link Title} entity if found, or an empty {@link Optional} if not found.
     */
    @Override
    public Optional<Title> findById(TitleId id) {
        return titleRepository.findById(id);
    }

    /**
     * Saves or updates an {@link Title} entity in the database.
     *
     * @param title The {@link Title} entity to be saved or updated.
     * @return The saved or updated {@link Title} entity.
     */
    @Override
    public Title save(Title title) {
        return titleRepository.save(title);
    }

    /**
     * Deletes an {@link Title} entity from the database by its unique identifier.
     *
     * @param id The unique identifier of the {@link Title} entity to be deleted.
     * @return No return value, as the operation is void.
     */
    @Override
    public void deleteById(TitleId id) {
        titleRepository.deleteById(id);
    }
}
