package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1.1/like")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @Operation(summary = "Like a post with a given ID")
    @PostMapping
    public void like(@RequestBody Like l) {
        likeService.like(l);
    }

    @Operation(summary = "Remove a user's like from a post", responses = {
            @ApiResponse(responseCode = "200", description = "Like was successfully removed"),
            @ApiResponse(responseCode = "404", description = "User id and/or post id are invalid")
        })
    @DeleteMapping(path = "{userID}/{postID}")
    public void removeLike(@PathVariable UUID userID, @PathVariable UUID postID) {
        likeService.removeLike(userID, postID);
    }
}
