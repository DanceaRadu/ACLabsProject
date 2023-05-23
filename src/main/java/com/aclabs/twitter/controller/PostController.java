package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public void post(@RequestBody Post post) {
        postService.post(post);
    }

    @Operation(summary = "Delete a post from the app", responses = {
            @ApiResponse(responseCode = "200", description = "Post deleted"),
            @ApiResponse(responseCode = "404", description = "No post was found for the given id")
    })
    @DeleteMapping(path = "{postID}")
    public void deletePost(@PathVariable Long postID) {
        postService.deletePost(postID);
    }

    //@PostMapping(path = "{postID}")
}
