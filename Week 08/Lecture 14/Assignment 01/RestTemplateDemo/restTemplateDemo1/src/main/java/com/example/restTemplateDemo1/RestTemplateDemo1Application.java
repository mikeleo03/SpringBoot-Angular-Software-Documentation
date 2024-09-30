package com.example.restTemplateDemo1;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestTemplateDemo1Application {

	public static void main(String[] args) {
        SpringApplication app = new SpringApplication(RestTemplateDemo1Application.class);
        app.setDefaultProperties(Collections.singletonMap("server.servlet.encoding.charset", "ISO-8859-1"));
        app.run(args);
    }
}
