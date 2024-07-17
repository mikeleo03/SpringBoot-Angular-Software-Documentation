package com.example.lecture_11.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecture_11.data.model.Title;
import com.example.lecture_11.data.model.composite.TitleId;
import com.example.lecture_11.services.TitleService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/titles")
@AllArgsConstructor
public class TitleController {

    private final TitleService titleService;

    /**
     * This method retrieves a {@link Title} from the database by its unique identifier.
     *
     * @param id The unique identifier of the {@link Title} to be retrieved.
     * @return ResponseEntity<Title> - A response entity containing the {@link Title} if found, or a 404 Not Found status code if not found.
     */
    @GetMapping
    public ResponseEntity<Title> findTitleById(@RequestBody TitleId id) {
        Optional<Title> titleOpt= titleService.findById(id);

        if(titleOpt.isPresent()) {
            return ResponseEntity.ok(titleOpt.get());
        }

        return ResponseEntity.notFound().build();
    }


    /**
     * This method saves a {@link Title} to the database.
     *
     * @param title The title object to be saved.
     * @return ResponseEntity<Title> - A response entity containing the saved {@link Title}.
     * If the {@link Title} already exists in the database, it returns a HTTP status code 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Title> save(@RequestBody Title title) {
        return ResponseEntity.ok(titleService.save(title));
    }

    /**
     * This method updates an existing {@link Title} in the database.
     *
     * @param title The title object to be updated.
     * @return ResponseEntity<Title> - A response entity containing the updated {@link Title}.
     * If the {@link Title} does not exist in the database, it returns a HTTP status code 404 (Not Found).
     */
    @PutMapping
    public ResponseEntity<Title> update(@RequestBody Title title) {
        Optional<Title> titleOpt = titleService.findById(title.getId());
        
        if (titleOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(titleService.save(title));
    }

    /**
     * This method deletes a {@link Title} from the database by its unique identifier.
     *
     * @param id The unique identifier of the {@link Title} to be deleted.
     * @return ResponseEntity<Title> - A response entity containing the deleted {@link Title} if found and successfully deleted, or a 404 Not Found status code if not found.
     */
    @DeleteMapping
    public ResponseEntity<Title> deleteTitle(@RequestBody TitleId id) {
        Optional<Title> titleOpt = titleService.findById(id);

        if (titleOpt.isPresent()) {
            titleService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
