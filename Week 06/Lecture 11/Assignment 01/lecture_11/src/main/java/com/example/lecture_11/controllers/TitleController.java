package com.example.lecture_11.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
     * This method saves or updates a {@link Title} in the database.
     *
     * @param title The {@link Title} object to be saved or updated.
     * @return ResponseEntity<Title> - A response entity containing the saved or updated {@link Title} if successful, or a 400 Bad Request status code if the {@link Title} with the same id already exists in the database.
     */
    @PostMapping
    public ResponseEntity<Title> saveOrUpdate(@RequestBody Title title) {
        Optional<Title> titleOpt = titleService.findById(title.getId());

        if (titleOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(titleService.saveOrUpdate(title));
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
