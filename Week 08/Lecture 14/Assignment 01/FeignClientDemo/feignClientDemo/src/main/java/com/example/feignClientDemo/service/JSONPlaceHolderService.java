package com.example.feignClientDemo.service;

import java.util.List;

import com.example.feignClientDemo.model.Post;

public interface JSONPlaceHolderService {

    List<Post> getPosts();

    Post getPostById(Long id);
}