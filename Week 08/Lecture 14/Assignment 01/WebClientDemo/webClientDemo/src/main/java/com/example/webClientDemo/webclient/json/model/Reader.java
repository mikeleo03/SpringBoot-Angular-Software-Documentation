package com.example.webClientDemo.webclient.json.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Reader {
    private final int id;
    private final String name;
    private final Book favouriteBook;
    private final List<Book> booksRead;

    @JsonCreator
    public Reader(
      @JsonProperty("id") int id,
      @JsonProperty("name") String name,
      @JsonProperty("favouriteBook") Book favouriteBook,
      @JsonProperty("booksRead") List<Book> booksRead) {
        this.id = id;
        this.name = name;
        this.favouriteBook = favouriteBook;
        this.booksRead =booksRead;
    }
}
