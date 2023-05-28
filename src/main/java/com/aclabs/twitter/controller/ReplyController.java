package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.service.ReplyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1.1/reply")
public class ReplyController {

    private final ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService) { this.replyService = replyService; }

    @Operation(summary = "add a reply to a post")
    @PostMapping
    public void addReply(@RequestBody Reply reply) {
        replyService.addReply(reply);
    }
}
