package com.example.spring_oauth2.config.filter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.spring_oauth2.data.model.ApiKey;
import com.example.spring_oauth2.data.repository.ApiKeyRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    private final ApiKeyRepository apiKeyRepository;
    private static final Logger loggerFilter = LoggerFactory.getLogger(ApiKeyFilter.class);

    @Autowired
    public ApiKeyFilter(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        loggerFilter.info("[Filter][{}][{}] {}", request, request.getMethod(), request.getRequestURI());
        
        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("PUT")) {
            filterChain.doFilter(request, response);
            return;
        }

        String requestURI = request.getRequestURI();

        if (!requestURI.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        loggerFilter.info("[Filter] None of init filter.");
        
        String requestApiKey = request.getHeader("api-key");

        Optional<ApiKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
        response.addHeader("source", "fpt-software");
        if (apiKeyOpt.isPresent()) {
            String storedApiKey = apiKeyOpt.get().getApiKey();

            loggerFilter.info("[Filter] storedApiKey: {}", storedApiKey);
            loggerFilter.info("[Filter] requestApiKey: {}", requestApiKey);

            if (storedApiKey.equals(requestApiKey)) {
                loggerFilter.info("[Filter] doFilter starts.");
                filterChain.doFilter(request, response);
                loggerFilter.info("[Filter] doFilter done.");
            } else {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid API Key\"}");
            }
        } else {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"API Key not configured or inactive\"}");
        }

        loggerFilter.info("[Filter] Logging Response: {}", response.getStatus());
    }
}