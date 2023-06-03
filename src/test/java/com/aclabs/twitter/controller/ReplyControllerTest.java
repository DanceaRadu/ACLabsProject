package com.aclabs.twitter.controller;

import com.aclabs.twitter.exceptionhandling.advice.AppAdvice;
import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplyController.class)
public class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ReplyService replyService;

    @BeforeEach
    public void setup() {
        ReplyController replyController = new ReplyController(replyService);
        mockMvc = MockMvcBuilders.standaloneSetup(replyController).setControllerAdvice(new AppAdvice()).build();
    }

    @Test
    public void addReplyTest() throws Exception{

        UUID userUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        User user = new User(userUUID);
        Post post = new Post(postUUID);

        Reply reply = new Reply();
        reply.setReplier(user);
        reply.setParentPost(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reply)))
                .andExpect(status().isOk());

        Mockito.doThrow(new UserNotFoundException(userUUID)).when(replyService).addReply(any(Reply.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1.1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reply)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + userUUID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));

        Mockito.doThrow(new PostNotFoundException(postUUID)).when(replyService).addReply(any(Reply.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1.1/reply")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(reply)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Post with id: " + postUUID + " could not be found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
