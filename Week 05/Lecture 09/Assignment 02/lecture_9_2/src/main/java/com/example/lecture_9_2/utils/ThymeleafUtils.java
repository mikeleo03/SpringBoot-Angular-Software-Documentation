package com.example.lecture_9_2.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class ThymeleafUtils {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }
}

