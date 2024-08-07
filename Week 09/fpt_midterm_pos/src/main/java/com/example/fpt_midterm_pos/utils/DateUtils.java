package com.example.fpt_midterm_pos.utils;

import java.util.Date;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("d/M/yyyy");

    private DateUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Parses the given date string using the specified format and returns the corresponding LocalDate object.
     *
     * @param dateStr the date string to be parsed in the format "d/M/yyyy"
     * @return the parsed LocalDate object
     * @throws DateTimeParseException if the date string cannot be parsed according to the specified format
     */
    public static LocalDate parseDate(String dateStr) throws DateTimeParseException {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }
    
    /**
     * Formats the given LocalDate object using the specified date format and returns the corresponding formatted date string.
     *
     * @param date the LocalDate object to be formatted
     * @return the formatted date string in the format "d/M/yyyy"
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Converts the given Date object to a LocalDate object using the system default time zone.
     *
     * @param date the Date object to be converted
     * @return the LocalDate object corresponding to the given Date object
     */
    public static LocalDate formatDateToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}