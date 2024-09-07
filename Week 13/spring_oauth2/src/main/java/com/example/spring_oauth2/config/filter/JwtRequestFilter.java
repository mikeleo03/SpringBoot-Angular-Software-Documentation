package com.example.spring_oauth2.config.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.spring_oauth2.data.model.User;
import com.example.spring_oauth2.service.AuthService;
import com.example.spring_oauth2.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String APP_JSON = "application/json";
    
    private final AuthService authService;
    private final TokenService tokenService;
    private static final Logger loggerFilter = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    public JwtRequestFilter(AuthService authService, TokenService tokenService) {
        this.tokenService = tokenService;
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        loggerFilter.info("[JWTFilter][{}][{}] {}", request, request.getMethod(), request.getRequestURI());

        // Exclude the /api/v1/authentication endpoint and any nested routes
        if (requestURI.startsWith("/api/v1/authentication") || !requestURI.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equals("OPTIONS") || request.getMethod().equals("PUT")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT and validate it
        final String authorizationHeader = request.getHeader("Authorization");

        // Initialize the values
        String username;
        String jwt;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = tokenService.extractUsername(jwt);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(APP_JSON);
            response.getWriter().write("{\"error\": \"Authorization header is not defined\"}");
            return;
        }

        if (username != null) {
            User userDetails = authService.findByUsername(username);

            if (userDetails != null) {
                if (Boolean.TRUE.equals(tokenService.validateToken(jwt, userDetails))) {
                    // Only if JWT token is valid
                    loggerFilter.info("[JWTFilter] doFilter starts.");
                    filterChain.doFilter(request, response);
                    loggerFilter.info("[JWTFilter] doFilter done.");
                }
            } else {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType(APP_JSON);
                response.getWriter().write("{\"error\": \"User not found\"}");
                return;
            }
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(APP_JSON);
            response.getWriter().write("{\"error\": \"JWT token is invalid\"}");
            return;
        }

        loggerFilter.info("[JWTFilter] Logging Response : {}", response.getStatus());
    }
}
