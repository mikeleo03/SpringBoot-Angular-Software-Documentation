package com.example.gateway.client;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;

import reactor.core.publisher.Mono;

@Service
public class AuthClient {

    private final WebClient.Builder webClientBuilder;
    private final EurekaClient eurekaClient;
    private static final String SERVICE_NAME = "authentication-service";

    @Autowired
    public AuthClient(EurekaClient eurekaClient, WebClient.Builder webClientBuilder) {
        this.eurekaClient = eurekaClient;
        this.webClientBuilder = webClientBuilder;
    }

    /**
     * Validates the provided API key by making a GET request to the /validate endpoint.
     *
     * @param apiKey The API key to be validated.
     * @return A Mono publisher that emits a boolean value representing the validation result.
     *         If the API key is valid, the Mono will emit true. If the API key is invalid or the request fails, the Mono will emit false.
     */
    public Mono<Boolean> validateApiKey(String apiKey) {
        // Retrieve instances of the authentication service
        List<InstanceInfo> instances = eurekaClient.getApplication(SERVICE_NAME).getInstances();

        // Handle the case where no instances are found
        if (instances.isEmpty()) {
            return Mono.just(false); // or throw an exception
        }

        // Get the first available instance
        InstanceInfo service = instances.get(0);
        String hostName = service.getHostName();
        int port = service.getPort();

        // Construct the base URL for the WebClient
        URI url = URI.create("http://" + hostName + ":" + port + "/api/v1/auth");

        // Build the WebClient with the correct base URL
        WebClient webClient = webClientBuilder.baseUrl(url.toString()).build();

        // Make the request to validate the API key
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
