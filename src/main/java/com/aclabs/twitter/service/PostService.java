package com.aclabs.twitter.service;

import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void post(Post post) {
        postRepository.save(post);
    }

}
