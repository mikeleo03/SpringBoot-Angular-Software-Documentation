package com.example.feignClientDemo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.feignClientDemo.client.JSONPlaceHolderClient;
import com.example.feignClientDemo.model.Post;
import com.example.feignClientDemo.service.JSONPlaceHolderService;

@Service
public class JSONPlaceHolderServiceImpl implements JSONPlaceHolderService {

    @Autowired
    private JSONPlaceHolderClient jsonPlaceHolderClient;

    @Override
    public List<Post> getPosts() {
        return jsonPlaceHolderClient.getPosts();
    }

    @Override
    public Post getPostById(Long id) {
        return jsonPlaceHolderClient.getPostById(id);
    }
}