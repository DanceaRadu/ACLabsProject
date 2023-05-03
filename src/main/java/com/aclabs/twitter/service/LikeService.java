package com.aclabs.twitter.service;

import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public void like(Like l) {
        likeRepository.save(l);
    }
}
