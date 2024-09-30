package com.example.lecture_13.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer; 

import com.example.lecture_13.config.interceptor.APIKeyInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer { 
    // Register an interceptor with the registry
	private final APIKeyInterceptor apiKeyInterceptor;

    @Autowired
    public InterceptorConfig(APIKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor);
    }
}
