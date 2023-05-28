package com.aclabs.twitter.controller;

import com.aclabs.twitter.mapstruct.DTO.PostNoDateDTO;
import com.aclabs.twitter.mapstruct.DTO.RepostDTO;
import com.aclabs.twitter.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1.1/post")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Posts a message to the app")
    @PostMapping
    public void post(@RequestBody PostNoDateDTO post) {
        postService.post(post);
    }

    @Operation(summary = "Delete a post from the app", responses = {
            @ApiResponse(responseCode = "200", description = "Post deleted"),
            @ApiResponse(responseCode = "404", description = "No post was found for the given id")
    })
    @DeleteMapping(path = "{postID}")
    public void deletePost(@PathVariable UUID postID) {
        postService.deletePost(postID);
    }

    @PostMapping(path = "repost")
    public void repost(@RequestBody RepostDTO repost) {
        postService.repost(repost);
    }
}
