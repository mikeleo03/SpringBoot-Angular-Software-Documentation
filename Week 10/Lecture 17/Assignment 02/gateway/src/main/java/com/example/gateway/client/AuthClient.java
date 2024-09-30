package com.example.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

@Service
public class AuthClient {

    private final WebClient webClient;

    @Autowired
    public AuthClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8083/api/v1/auth").build();
    }

    /**
     * Validates the provided API key by making a GET request to the /validate endpoint.
     *
     * @param apiKey The API key to be validated.
     * @return A Mono publisher that emits a boolean value representing the validation result.
     *         If the API key is valid, the Mono will emit true. If the API key is invalid or the request fails, the Mono will emit false.
     */
    public Mono<Boolean> validateApiKey(String apiKey) {
        return webClient.get()
                .uri("/validate?key=" + apiKey)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode().is4xxClientError()) {
                        return Mono.just(false);
                    }
                    return Mono.error(ex);
                });
    }        
}
