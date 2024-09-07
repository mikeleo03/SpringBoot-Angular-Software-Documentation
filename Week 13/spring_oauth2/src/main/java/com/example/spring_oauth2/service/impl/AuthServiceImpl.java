package com.example.spring_oauth2.service.impl;

import com.example.spring_oauth2.data.model.User;
import com.example.spring_oauth2.data.repository.UserRepository;
import com.example.spring_oauth2.service.AuthService;
import com.example.spring_oauth2.utils.PasswordUtils;
import com.example.spring_oauth2.exception.AuthException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a user from the repository based on the provided username.
     *
     * @param username The unique identifier of the user to be retrieved.
     * @return A {@link User} object representing the user with the given username.
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void authenticate(String username, String password) throws AuthException {
        // Find the user from the database
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AuthException("User not found, please input valid username");
        }

        // Compare the password
        if (!PasswordUtils.verifyPassword(password, user.getPassword())) {
            throw new AuthException("Password wrong, please try again");
        }
    }

    @Override
    public void saveUser(User user) {
        user.setPassword(PasswordUtils.hashPassword(user.getPassword())); // Hash the password before saving
        userRepository.save(user);
    }
}
