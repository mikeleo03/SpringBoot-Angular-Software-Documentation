package com.example.lecture_13.config.filter;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.lecture_13.data.model.APIKey;
import com.example.lecture_13.data.repository.APIKeyRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class APIKeyFilter extends OncePerRequestFilter {

    @Autowired
    private APIKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        String requestApiKey = request.getHeader("api-key");

        Optional<APIKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
        response.addHeader("source", "fpt-software");
        if (apiKeyOpt.isPresent()) {
            String storedApiKey = apiKeyOpt.get().getApiKey();

            if (storedApiKey.equals(requestApiKey)) {
                filterChain.doFilter(request, response);
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
    }
}
