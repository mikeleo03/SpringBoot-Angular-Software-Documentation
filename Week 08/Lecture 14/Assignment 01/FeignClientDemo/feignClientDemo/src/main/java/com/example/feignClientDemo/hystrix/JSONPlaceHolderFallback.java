package com.example.feignClientDemo.hystrix;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.feignClientDemo.client.JSONPlaceHolderClient;
import com.example.feignClientDemo.model.Post;

@Component
public class JSONPlaceHolderFallback implements JSONPlaceHolderClient {

    @Override
    public List<Post> getPosts() {
        return Collections.emptyList();
    }

    @Override
    public Post getPostById(Long postId) {
        return null;
    }
}
