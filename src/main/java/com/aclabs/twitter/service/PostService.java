package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.mapstruct.DTO.RepostDTO;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public void post(Post post) {
        System.out.println("here");
        if(!userRepository.existsById(post.getPoster().getUserID())) throw new UserNotFoundException(post.getPoster().getUserID());
        postRepository.save(post);
    }
    public void deletePost(UUID postID) {
        if(!postRepository.existsById(postID)) throw new PostNotFoundException(postID);
        else postRepository.deleteById(postID);
    }

    public void repost(RepostDTO repost) {
        if(!postRepository.existsById(repost.getPostID())) throw new PostNotFoundException(repost.getPostID());
        if(!userRepository.existsById(repost.getReposterID())) throw new UserNotFoundException(repost.getReposterID());

        Post newPost = new Post();
        Post oldPost = postRepository.getReferenceById(repost.getPostID());
        newPost.setPostDate(new Timestamp(new Date().getTime()));
        newPost.setPoster(userRepository.getReferenceById(repost.getReposterID()));
        newPost.setMessage(oldPost.getMessage());
        postRepository.save(newPost);
    }

}
