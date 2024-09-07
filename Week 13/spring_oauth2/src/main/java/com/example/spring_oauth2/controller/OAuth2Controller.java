package com.example.spring_oauth2.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.spring_oauth2.dto.JwtResponse;
import com.example.spring_oauth2.exception.AuthException;
import com.example.spring_oauth2.service.TokenService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/oauth2")
public class OAuth2Controller {

    private final TokenService tokenService;

    @Value("${spring.security.oauth2.client.registration.google.user-info-uri}")
    private String userInfoUri;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String gClientID;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String gClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectURI;

    @Autowired
    public OAuth2Controller(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/callback")
    public ResponseEntity<JwtResponse> handleOAuth2Callback(@RequestParam("code") String code) {
        try {
            // Exchange authorization code for access token
            String accessToken = getAccessToken(code);

            // Fetch user info from Google using the access token
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map<String, Object>> userInfoResponse = restTemplate.exchange(
                userInfoUri + "?access_token=" + accessToken, 
                org.springframework.http.HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            Map<String, Object> userInfo = userInfoResponse.getBody();

            if (userInfo == null) {
                throw new AuthException("Failed to retrieve user info");
            }

            // Extract user information
            String subject = (String) userInfo.get("name"); // Google user name

            // Generate JWT token
            final String jwt = tokenService.generateToken(subject);

            // Return the token as a response
            return ResponseEntity.status(HttpStatus.OK).body(new JwtResponse(jwt));
        } catch (RestClientException e) {
            throw new AuthException("Error processing authentication");
        }
    }

    private String getAccessToken(String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = "https://oauth2.googleapis.com/token";
    
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", gClientID);
        body.add("client_secret", gClientSecret);
        body.add("redirect_uri", redirectURI);
        body.add("grant_type", "authorization_code");
    
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);
    
        // Using exchange() with ParameterizedTypeReference
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(
            requestUrl,
            org.springframework.http.HttpMethod.POST,
            requestEntity,
            new ParameterizedTypeReference<Map<String, String>>() {}
        );
    
        Map<String, String> responseBody = response.getBody();
    
        if (responseBody != null) {
            return responseBody.get("access_token");
        } else {
            throw new RestClientException("Failed to retrieve access token");
        }
    }    
}