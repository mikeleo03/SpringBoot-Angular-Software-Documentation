package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {
    private static final DateTimeFormatter DATE_FORMATER = DateTimeFormatter.ofPattern("d/M/yyyy");

    /**
     * Parses a date string in the format "d/M/yyyy" and returns a LocalDate object.
     *
     * @param dateString the date string to be parsed
     * @return the parsed LocalDate object
     * @throws IllegalArgumentException if the date string cannot be parsed
     */
    public static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DATE_FORMATER);
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + dateStr);
            throw e;
        }
    }

    /**
     * Formats the given LocalDate object into a string in the format "d/M/yyyy".
     *
     * @param date the LocalDate object to be formatted
     * @return the formatted string in the specified format
     */
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATER);
    }
}