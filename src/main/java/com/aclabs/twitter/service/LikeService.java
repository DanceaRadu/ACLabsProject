package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.LikeNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.repository.LikeRepository;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public void like(Like l) {
        likeRepository.save(l);
    }

    public void removeLike(UUID userID, UUID postID) {
        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        if(!postRepository.existsById(postID)) throw new PostNotFoundException(postID);

        Like.LikeID id = new Like.LikeID(userID, postID);
        if(!likeRepository.existsById(id)) throw new LikeNotFoundException(userID, postID);
        likeRepository.deleteById(id);
    }
}
