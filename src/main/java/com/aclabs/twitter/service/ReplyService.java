package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.ReplyRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository, PostRepository postRepository, UserRepository userRepository) {
        this.replyRepository = replyRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
}

    public void addReply(Reply reply) {
        if(!postRepository.existsById(reply.getParentPost().getId())) throw new PostNotFoundException(reply.getParentPost().getId());
        if(!userRepository.existsById(reply.getReplier().getUserID())) throw new UserNotFoundException(reply.getReplier().getUserID());
        reply.setPostDate(new Timestamp(new Date().getTime()));
        replyRepository.save(reply);
    }
}
