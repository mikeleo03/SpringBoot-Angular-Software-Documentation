package com.example.spring_oauth2.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    public SecurityConfig(CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {
                CorsConfigurationSource source = request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:3000")); // Specify allowed origins
                    config.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
                    config.setAllowedHeaders(List.of("Authorization", "Content-Type", "api-key")); // Include your custom 'api-key' header
                    config.setAllowCredentials(true); // If you need cookies or authentication
                    return config;
                };
                cors.configurationSource(source);
            })
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // Use HttpMethod instead of RequestMethod
                .requestMatchers("/api/v1/**", "/oauth2/**", "/login/**", "/").permitAll()  // Allow access to all these endpoints
                .anyRequest().authenticated()
            )
            .oauth2Login(authz -> authz
                .successHandler(customAuthenticationSuccessHandler)  // Custom success handler for OAuth
                .defaultSuccessUrl("/api/v1/authentication")  // OAuth success redirection
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );
        
        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs").build();
    }

    // CORS configuration bean
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "OPTIONS", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "api-key"));
        configuration.setAllowCredentials(true); // Important for cookies or sessions

        // Avoiding redirects during preflight
        configuration.setExposedHeaders(List.of("Location"));  // Expose location in case of redirects
        configuration.setMaxAge(3600L);  // Cache preflight response for 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }
}