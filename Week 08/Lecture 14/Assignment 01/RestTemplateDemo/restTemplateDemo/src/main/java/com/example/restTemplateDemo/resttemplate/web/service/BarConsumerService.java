package com.example.restTemplateDemo.resttemplate.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.restTemplateDemo.resttemplate.web.handler.RestTemplateRespErrorHandler;
import com.example.restTemplateDemo.resttemplate.web.model.Bar;

@Service
public class BarConsumerService {

    private final RestTemplate restTemplate;

    @Autowired
    public BarConsumerService(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
          .errorHandler(new RestTemplateRespErrorHandler())
          .build();
    }

    public Bar fetchBarById(String barId) {
        return restTemplate.getForObject("/bars/4242", Bar.class);
    }
}
