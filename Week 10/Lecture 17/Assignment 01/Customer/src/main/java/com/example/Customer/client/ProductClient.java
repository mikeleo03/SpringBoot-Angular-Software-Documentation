package com.example.customer.client;

import com.example.customer.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class ProductClient {

    private final WebClient webClient;

    @Autowired
    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/v1/products").build();
    }

    public ProductDTO getProductById(UUID productId) {
        return this.webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();
    }

    public List<ProductDTO> getProductsByCustomerId(UUID customerId) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/by-customer")
                        .queryParam("customerId", customerId)
                        .build())
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .block();
    }

    public void reduceProductQuantity(UUID productId, int quantity) {
        this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/reduce-quantity")
                        .queryParam("productId", productId)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}