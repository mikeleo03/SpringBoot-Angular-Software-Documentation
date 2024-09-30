package com.example.webClientDemo.reactive.webclient.simultaneous;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Item {
    private int id;

    @JsonCreator
    public Item(@JsonProperty("id") int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}