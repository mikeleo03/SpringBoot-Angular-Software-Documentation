package com.example.feignClientDemo.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;

import com.example.feignClientDemo.model.Todo;

import feign.RequestLine;

@FeignClient(name = "todoClient")
public interface TodoClient {
    @RequestLine(value = "GET")
    Todo getTodoById(URI uri);
}
