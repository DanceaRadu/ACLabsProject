package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.FollowRelationNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.InvalidFollowException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.repository.FollowRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public void follow(Follow follow) {
        if(!userRepository.existsById(follow.getFollowedUser().getUserID())) throw new UserNotFoundException(follow.getFollowedUser().getUserID());
        if(!userRepository.existsById(follow.getFollowerUser().getUserID())) throw new UserNotFoundException(follow.getFollowerUser().getUserID());
        if(follow.getFollowerUser().getUserID().equals(follow.getFollowedUser().getUserID())) throw new InvalidFollowException();

        followRepository.save(follow);
    }
    public void unfollow(UUID userID, UUID unfollowedUserID) {
        Optional<Follow> follow = followRepository.getFollowByFollowerUser_UserIDAndFollowedUser_UserID(userID, unfollowedUserID);
        if(follow.isEmpty()) throw new FollowRelationNotFoundException(userID, unfollowedUserID);
        follow.ifPresent(value -> followRepository.deleteById(value.getFollowID()));
    }
}
