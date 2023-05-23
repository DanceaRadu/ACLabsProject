package com.aclabs.twitter.service;

import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    private final ReplyRepository replyRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository) {
        this.replyRepository = replyRepository;
    }

    public void addReply(Reply reply) {
        replyRepository.save(reply);
    }
}
