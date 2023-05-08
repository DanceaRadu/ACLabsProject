package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/like")
public class LikeController {

    private final LikeService likeService;

    @Autowired
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    @PostMapping
    public void like(@RequestBody Like l) {
        likeService.like(l);
    }

}
