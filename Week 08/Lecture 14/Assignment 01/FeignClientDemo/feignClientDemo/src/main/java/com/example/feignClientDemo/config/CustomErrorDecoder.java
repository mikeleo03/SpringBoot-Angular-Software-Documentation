package com.example.feignClientDemo.config;

import com.example.feignClientDemo.exception.BadRequestException;
import com.example.feignClientDemo.exception.NotFoundException;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String methodKey, Response response) {

        return switch (response.status()) {
            case 400 -> new BadRequestException();
            case 404 -> new NotFoundException("Not found !!!");
            default -> new Exception("Generic error");
        };
    }
}
