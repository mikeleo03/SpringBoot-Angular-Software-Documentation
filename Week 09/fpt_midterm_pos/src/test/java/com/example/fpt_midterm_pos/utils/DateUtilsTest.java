package com.example.fpt_midterm_pos.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.junit.jupiter.api.Test;

public class DateUtilsTest {

    @Test
    public void testParseDate_withValidDate() {
        String validDateStr = "15/8/2024";
        LocalDate expectedDate = LocalDate.of(2024, 8, 15);
        LocalDate parsedDate = DateUtils.parseDate(validDateStr);
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void testParseDate_withInvalidDate() {
        String invalidDateStr = "invalid-date";
        assertThrows(DateTimeParseException.class, () -> {
            DateUtils.parseDate(invalidDateStr);
        });
    }

    @Test
    public void testFormatDate_withValidDate() {
        LocalDate date = LocalDate.of(2024, 8, 15);
        String expectedDateStr = "15/8/2024";
        String formattedDateStr = DateUtils.formatDate(date);
        assertEquals(expectedDateStr, formattedDateStr);
    }

    @Test
    public void testFormatDateToLocalDate_withValidDate() {
        Date date = new Date();
        LocalDate expectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate = DateUtils.formatDateToLocalDate(date);
        assertEquals(expectedDate, localDate);
    }
}
