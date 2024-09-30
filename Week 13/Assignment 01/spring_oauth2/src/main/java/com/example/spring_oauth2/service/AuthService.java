package com.example.spring_oauth2.service;

import com.example.spring_oauth2.data.model.User;
import com.example.spring_oauth2.exception.AuthException;

public interface AuthService {

    // Find user by its username
    User findByUsername(String username);

    // Authenticate user based on given user data
    void authenticate(String username, String password) throws AuthException;

    // Save the user to the database
    void saveUser(User user);
}
