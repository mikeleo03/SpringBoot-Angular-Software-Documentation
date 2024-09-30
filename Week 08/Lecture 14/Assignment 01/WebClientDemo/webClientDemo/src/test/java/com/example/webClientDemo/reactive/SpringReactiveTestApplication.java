package com.example.webClientDemo.reactive;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webClientDemo.reactive.model.Foo;

@SpringBootApplication
public class SpringReactiveTestApplication {

    @Bean
    public WebClient client() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    CommandLineRunner cmd(WebClient client) {
        return args -> {
            client.get().uri("/foos2")
                .retrieve()
                .bodyToFlux(Foo.class).log()
                .subscribe(System.out::println);
        };
    }
    
    // 
    public static void main(String[] args) {
        SpringApplication.run(SpringReactiveTestApplication.class, args);
    }
    
}
