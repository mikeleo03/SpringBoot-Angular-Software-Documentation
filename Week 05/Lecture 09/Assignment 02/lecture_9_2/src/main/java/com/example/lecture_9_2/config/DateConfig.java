package com.example.lecture_9_2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.lecture_9_2.utils.DateUtils;
import com.example.lecture_9_2.utils.ThymeleafUtils;

@Configuration
public class DateConfig {

    @Bean
    public DateUtils dateUtils() {
        return new DateUtils();
    }

    @Bean
    public ThymeleafUtils thymeleafUtils() {
        return new ThymeleafUtils();
    }
}

