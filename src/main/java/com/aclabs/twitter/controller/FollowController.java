package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
