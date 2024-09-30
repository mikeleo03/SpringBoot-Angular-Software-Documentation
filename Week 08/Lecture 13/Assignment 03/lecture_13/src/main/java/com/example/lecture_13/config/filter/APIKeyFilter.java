package com.example.lecture_13.config.filter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(APIKeyFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
            throws ServletException, IOException {
        
        logger.info("[Filter][" + request + "]" + "[" + request.getMethod()+ "] " + request.getRequestURI());
        String requestApiKey = request.getHeader("api-key");

        Optional<APIKey> apiKeyOpt = apiKeyRepository.findFirstByActiveTrueOrderById();
        response.addHeader("source", "fpt-software");
        if (apiKeyOpt.isPresent()) {
            String storedApiKey = apiKeyOpt.get().getApiKey();

            if (storedApiKey.equals(requestApiKey)) {
                logger.info("[Filter] doFilter starts.");
                filterChain.doFilter(request, response);
                logger.info("[Filter] doFilter done.");
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

        logger.info("[Filter] Logging Response : {}", response.getStatus());
    }
}
