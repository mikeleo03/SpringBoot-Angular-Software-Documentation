package com.example.customer.client;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.customer.data.model.CustomerProduct;
import com.example.customer.dto.ProductDTO;
import com.example.customer.exception.BadRequestException;
import com.example.customer.exception.InsufficientQuantityException;
import com.example.customer.exception.ResourceNotFoundException;

@Service
public class ProductClient {

    private final WebClient webClient;

    @Autowired
    public ProductClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8081/api/v1/products").build();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId The ID of the product to retrieve.
     * @return The retrieved product as a ProductDTO.
     * @throws ResourceNotFoundException If the product is not found.
     * @throws BadRequestException If there's an error communicating with the product service.
     */
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

    /**
     * Retrieves a list of products associated with a customer.
     *
     * @param customerId The ID of the customer.
     * @return A list of products associated with the customer.
     * @throws ResourceNotFoundException If the customer or products are not found.
     * @throws BadRequestException If there's an error communicating with the product service.
     */
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

    /**
     * Reduces the quantity of a product.
     *
     * @param productId The ID of the product to reduce quantity for.
     * @param quantity The amount to reduce the quantity by.
     * @throws InsufficientQuantityException If the product's quantity is insufficient.
     * @throws BadRequestException If there's an error communicating with the product service.
     */
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
                throw new InsufficientQuantityException(errorMessage);
            } else {
                throw new BadRequestException("Failed to reduce product quantity" + ex.getMessage());
            }
        }
    }

    /**
     * Saves a customer-product association.
     *
     * @param customerProduct The customer-product association to save.
     * @throws BadRequestException If there's an error communicating with the product service.
     */
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