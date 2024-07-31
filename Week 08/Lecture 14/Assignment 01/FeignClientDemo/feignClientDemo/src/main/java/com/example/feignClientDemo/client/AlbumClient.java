package com.example.feignClientDemo.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.feignClientDemo.model.Album;

@FeignClient(name = "albumClient", url = "https://jsonplaceholder.typicode.com/albums/")
public interface AlbumClient {
    @GetMapping(value = "/{id}")
    Album getAlbumById(@PathVariable(value = "id") Integer id);

    @GetMapping(value = "/{id}")
    Album getAlbumByIdAndDynamicUrl(@PathVariable(name = "id") Integer id);
}
