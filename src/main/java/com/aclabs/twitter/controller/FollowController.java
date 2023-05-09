package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1.1/follow")
public class FollowController {

    private final FollowService followService;

    @Autowired
    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @Operation(summary = "Create a follow relationship between 2 users")
    @PostMapping
    public void follow(@RequestBody Follow f) {
        followService.follow(f);
    }

    @Operation(summary = "Stop following a user", responses = {
            @ApiResponse(responseCode = "200", description = "Unfollow successful"),
            @ApiResponse(responseCode = "404", description = "No follow relation was found between the two users")
    })
    @DeleteMapping(path = "{userID}/{unfollowedUserID}")
    public void unfollow(@PathVariable long userID, @PathVariable long unfollowedUserID) {
        followService.unfollow(userID, unfollowedUserID);
    }
}
