package com.example.lecture_13.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.lecture_13.config.filter.APIKeyFilter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<APIKeyFilter> apiKeyFilter(APIKeyFilter apiKeyFilter) {
        FilterRegistrationBean<APIKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/v1/*"); // All Customer API
        return registrationBean;
    }
}