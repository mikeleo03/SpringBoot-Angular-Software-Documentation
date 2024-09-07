package com.example.spring_oauth2.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    public ResponseEntity<?> handleOAuth2Callback(@RequestParam("code") String code) {
        try {
            // Exchange authorization code for access token
            String accessToken = getAccessToken(code);

            // Fetch user info from Google using the access token
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoUri + "?access_token=" + accessToken, Map.class);
            Map<String, Object> userInfo = userInfoResponse.getBody();

            // Extract user information
            String subject = (String) userInfo.get("sub"); // Google user ID

            // Generate JWT token
            String token = tokenService.generateToken(subject);
            return ResponseEntity.ok(Collections.singletonMap("token", token));
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing authentication");
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
        ResponseEntity<Map> response = restTemplate.postForEntity(requestUrl, requestEntity, Map.class);
        Map<String, String> responseBody = response.getBody();
        return responseBody.get("access_token");
    }
}