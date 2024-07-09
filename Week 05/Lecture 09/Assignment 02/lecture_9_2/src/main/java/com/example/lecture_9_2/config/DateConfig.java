package com.example.lecture_9_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.lecture_9_2.utils.DateUtils;
import com.example.lecture_9_2.utils.ThymeleafUtils;

@Configuration
public class DateConfig {

    /**
     * This method creates and returns an instance of the DateUtils class.
     * The DateUtils class provides utility methods for working with dates.
     *
     * @return An instance of the DateUtils class.
     */
    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }

    /**
     * This method creates and returns an instance of the ThymeleafUtils class.
     * The ThymeleafUtils class provides utility methods for working with Thymeleaf templates.
     *
     * @return An instance of the ThymeleafUtils class.
     */
    @Bean
    public ThymeleafUtils thymeleafUtils() {
        return new ThymeleafUtils();
    }
}

