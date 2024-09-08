package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.example.gateway.client.AuthClient;

import reactor.core.publisher.Mono;

@Component
public class ApiKeyGatewayFilterFactory extends AbstractGatewayFilterFactory<ApiKeyGatewayFilterFactory.Config> {

    private static final String API_KEY_HEADER = "api-key";
    private final AuthClient authClient;

    @Autowired
    public ApiKeyGatewayFilterFactory(AuthClient authClient) {
        super(Config.class);
        this.authClient = authClient;
    }

    /**
     * Applies the API key gateway filter to the incoming request.
     * This filter validates the API key provided in the request header against the authentication service.
     * If the API key is not present or invalid, it returns an HTTP 401 Unauthorized response.
     *
     * @param config The configuration for the filter. Currently, no configuration properties are defined.
     * @return A GatewayFilter that can be applied to the request/response chain.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String apiKey = exchange.getRequest().getHeaders().getFirst(API_KEY_HEADER);
            if (apiKey == null) {
                return Mono.just(exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)).then();
            }

            return authClient.validateApiKey(apiKey)
                .flatMap(isValid -> {
                    if (!isValid) {
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return Mono.empty();
                    } else {
                        return chain.filter(exchange);
                    }
                });
        };
    }

    @Override
    public Config newConfig() {
        return new Config();
    }

    public static class Config {
        // Configuration properties (if any) can be added here
    }
}