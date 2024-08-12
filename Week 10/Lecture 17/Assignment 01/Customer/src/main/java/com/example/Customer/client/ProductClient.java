package com.example.customer.client;

import com.example.customer.data.model.CustomerProduct;
import com.example.customer.dto.ProductDTO;
import com.example.customer.exception.BadRequestException;
import com.example.customer.exception.InsufficientProductQuantityException;
import com.example.customer.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@Service
public class ProductClient {

    private final WebClient webClient;

    @Autowired
    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/v1/products").build();
    }

    public ProductDTO getProductById(String productId) {
        try {
            return this.webClient.get()
                .uri("/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDTO.class)
                .block();
        } catch (WebClientResponseException ex) {
            // Handle the specific error status and throw a custom exception
            if (ex.getStatusCode().is4xxClientError()) {
                String errorMessage = extractErrorMessage(ex.getResponseBodyAsString());
                throw new ResourceNotFoundException(errorMessage);
            } else {
                throw new BadRequestException("Failed to retrieve products" + ex.getMessage());
            }
        }
    }

    public List<ProductDTO> getProductsByCustomerId(String customerId) {
        try {
            return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/by-customer")
                        .queryParam("customerId", customerId)
                        .build())
                .retrieve()
                .bodyToFlux(ProductDTO.class)
                .collectList()
                .block();
        } catch (WebClientResponseException ex) {
            // Handle the specific error status and throw a custom exception
            if (ex.getStatusCode().is4xxClientError()) {
                String errorMessage = extractErrorMessage(ex.getResponseBodyAsString());
                throw new ResourceNotFoundException(errorMessage);
            } else {
                throw new BadRequestException("Failed to retrieve products" + ex.getMessage());
            }
        }
    }

    public void reduceProductQuantity(String productId, int quantity) {
        try {
            this.webClient.post()
                .uri(uriBuilder -> uriBuilder
                    .path("/reduce-quantity")
                    .queryParam("productId", productId)
                    .queryParam("quantity", quantity)
                    .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (WebClientResponseException ex) {
            // Handle the specific error status and throw a custom exception
            if (ex.getStatusCode().is4xxClientError()) {
                String errorMessage = extractErrorMessage(ex.getResponseBodyAsString());
                throw new InsufficientProductQuantityException(errorMessage);
            } else {
                throw new BadRequestException("Failed to reduce product quantity" + ex.getMessage());
            }
        }
    }

    public void saveCustomerProduct(CustomerProduct customerProduct) {
        try {
            this.webClient.post()
                .uri("/customer-products")
                .bodyValue(customerProduct)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
        } catch (WebClientResponseException ex) {
            throw new BadRequestException("Failed to save customer product in Product service: " + ex.getMessage());
        }
    }

    private String extractErrorMessage(String responseBody) {
        if (StringUtils.hasText(responseBody) && responseBody.contains("error")) {
            // Extract the value of the "error" field from the JSON response
            return responseBody.replaceAll(".*\"error\":\"([^\"]+)\".*", "$1");
        }
        return responseBody;
    }
}