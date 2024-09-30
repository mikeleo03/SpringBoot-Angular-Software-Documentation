package com.example.lecture_7_1.controller;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.lecture_7_1.service.RequestScopedBean;

@RestController
public class RequestController {

    private final ObjectFactory<RequestScopedBean> requestScopedBeanFactory;

    @Autowired
    public RequestController(ObjectFactory<RequestScopedBean> requestScopedBeanFactory) {
        this.requestScopedBeanFactory = requestScopedBeanFactory;
    }

    @GetMapping("/testRequest")
    public String testRequestScope() {
        RequestScopedBean requestScopedBean = requestScopedBeanFactory.getObject();
        requestScopedBean.handleRequest();
        return "Check the console for the RequestScopedBean hash code.";
    }
}