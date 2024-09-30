package com.example.spring_oauth2.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.spring_oauth2.config.filter.ApiKeyFilter;
import com.example.spring_oauth2.config.filter.JwtRequestFilter;
import com.example.spring_oauth2.data.repository.ApiKeyRepository;
import com.example.spring_oauth2.service.AuthService;
import com.example.spring_oauth2.service.TokenService;

@Configuration
public class FilterConfig {

    private final ApiKeyRepository apiKeyRepository;
    private final AuthService authService;
    private final TokenService tokenService;

    public FilterConfig(ApiKeyRepository apiKeyRepository, AuthService authService, TokenService tokenService) {
        this.apiKeyRepository = apiKeyRepository;
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @Bean(name = "apiKeyFilterBean")
    public ApiKeyFilter apiKeyFilter() {
        return new ApiKeyFilter(apiKeyRepository);
    }

    @Bean(name = "apiKeyFilterRegistrationBean")
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilterRegistration(ApiKeyFilter apiKeyFilter) {
        FilterRegistrationBean<ApiKeyFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(apiKeyFilter);
        registrationBean.addUrlPatterns("/api/*"); // All API
        return registrationBean;
    }

    @Bean(name = "jwtRequestFilterBean")
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(authService, tokenService);
    }

    @Bean(name = "jwtRequestFilterRegistrationBean")
    public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterRegistration(JwtRequestFilter jwtRequestFilter) {
        FilterRegistrationBean<JwtRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
        registrationBean.addUrlPatterns("/api/*"); // All API except /api/v1/authentication is handled in the filter
        return registrationBean;
    }
}
