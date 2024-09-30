package com.example.webClientDemo.reactive.webclient.simultaneous;

import lombok.Data;

@Data
public class UserWithItem {
    private User user;
    private Item item;

    public UserWithItem(User user, Item item) {
        this.user = user;
        this.item = item;
    }

    public User user() {
        return user;
    }

    public Item item() {
        return item;
    }
}
