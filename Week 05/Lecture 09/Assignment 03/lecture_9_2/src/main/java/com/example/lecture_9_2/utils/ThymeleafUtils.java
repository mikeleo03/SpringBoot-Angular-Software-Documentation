package com.example.lecture_9_2.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ThymeleafUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Formats the given LocalDate object using the specified date format pattern.
     *
     * @param date The LocalDate object to be formatted.
     * @return A String representation of the date in the format "yyyy-MM-dd".
     */
    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}

