package com.example.feignClientDemo.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.feignClientDemo.config.ClientConfiguration;
import com.example.feignClientDemo.hystrix.JSONPlaceHolderFallback;
import com.example.feignClientDemo.model.Post;

@FeignClient(value = "jplaceholder", url = "https://jsonplaceholder.typicode.com/", configuration = ClientConfiguration.class, fallback = JSONPlaceHolderFallback.class)
public interface JSONPlaceHolderClient {

    @RequestMapping(method = RequestMethod.GET, value = "/posts")
    List<Post> getPosts();

    @RequestMapping(method = RequestMethod.GET, value = "/posts/{postId}", produces = "application/json")
    Post getPostById(@PathVariable("postId") Long postId);
}
