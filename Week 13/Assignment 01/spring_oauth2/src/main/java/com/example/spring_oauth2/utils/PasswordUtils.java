package com.example.spring_oauth2.utils;

import com.password4j.Hash;
import com.password4j.Password;

public class PasswordUtils {

    private PasswordUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Hashes a plain text password using a secure algorithm.
     *
     * @param plainPassword The plain text password.
     * @return The hashed password.
     */
    public static String hashPassword(String plainPassword) {
        Hash hash = Password.hash(plainPassword).withBCrypt();
        return hash.getResult();
    }

    /**
     * Verifies a plain text password against a hashed password.
     *
     * @param plainPassword The plain text password.
     * @param hashedPassword The hashed password.
     * @return True if the passwords match, false otherwise.
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return Password.check(plainPassword, hashedPassword).withBCrypt();
    }
}

