package com.example.restTemplateDemo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restTemplateDemo.resttemplate.RestTemplateConfigurationApplication;

@SpringBootTest(classes= RestTemplateConfigurationApplication.class)
public class SpringContextTest {

    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}
