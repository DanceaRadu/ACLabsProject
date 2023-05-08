package com.aclabs.twitter.service;

import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    public void follow(Follow follow) {
        followRepository.save(follow);
    }

}
