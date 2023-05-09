package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.FollowRelationNotFoundException;
import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    public void unfollow(Long userID, Long unfollowedUserID) {
        Optional<Follow> follow = followRepository.getFollowByFollowerUser_UserIDAndFollowedUser_UserID(userID, unfollowedUserID);
        if(follow.isEmpty()) throw new FollowRelationNotFoundException(userID, unfollowedUserID);
        follow.ifPresent(value -> followRepository.deleteById(value.getFollowID()));
    }
}
