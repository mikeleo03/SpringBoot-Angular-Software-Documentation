package com.example.lecture_11.controllers;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     * This method retrieves {@link Page} of {@link Title} from the database.
     *
     * @return ResponseEntity<List<Title>> - A response entity containing a pages of {@link Title}.
     * If the pages is empty, it returns a HTTP status code 204 (No Content).
     * If the operation is successful, it returns a HTTP status code 200 (OK) with the pages of {@link Title}.
     */
    @GetMapping
    public ResponseEntity<Page<Title>> findAll(Pageable pageable) {
        Page<Title> titles = titleService.findAll(pageable);

        if (titles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(titles);
    }

     /**
     * This method retrieves an {@link Title} from the database by its id.
     *
     * @param id The unique identifier of the {@link Title}.
     * @return ResponseEntity<Title> - A response entity containing the {@link Title} if found, or a 404 Not Found status code if not found.
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Title> findTitleById(@PathVariable("id") TitleId id) {
        Optional<Title> titleOpt= titleService.findById(id);

        if(titleOpt.isPresent()) {
            return ResponseEntity.ok(titleOpt.get());
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * This method saves or updates a {@link Title} to the database.
     *
     * @param title The title object to be saved.
     * @return ResponseEntity<Title> - A response entity containing the saved {@link Title}.
     * If the {@link Title} already exists in the database, it returns a HTTP status code 400 (Bad Request).
     */
    @PostMapping
    public ResponseEntity<Title> saveOrUpdate(@RequestBody Title title) {
        TitleId titleId = new TitleId(title.getEmpNo(), title.getTitle(), title.getFromDate());
        Optional<Title> titleOpt = titleService.findById(titleId);
        
        if(titleOpt.isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(titleService.saveOrUpdate(title));
    }

    /**
     * This method deletes an {@link Title} from the database by its id.
     *
     * @param id The unique identifier of the {@link Title} to be deleted.
     * @return ResponseEntity<Title> - A response entity containing the deleted {@link Title} if found, or a 404 Not Found status code if not found.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Title> deleteTitle(@PathVariable(value = "id") TitleId id) {
        Optional<Title> titleOpt = titleService.findById(id);

        if(titleOpt.isPresent()) {
            titleService.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }
}
