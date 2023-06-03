package com.aclabs.twitter.controller;

import com.aclabs.twitter.exceptionhandling.advice.AppAdvice;
import com.aclabs.twitter.exceptionhandling.exceptions.LikeNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.LikeService;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LikeController.class)
public class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LikeService likeService;
    private String apiVersion;

    @BeforeEach
    public void setup() {
        LikeController likeController = new LikeController(likeService);
        mockMvc = MockMvcBuilders.standaloneSetup(likeController).setControllerAdvice(new AppAdvice()).build();

        try (InputStream input = new FileInputStream("src/test/resources/test.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiVersion = prop.getProperty("api-version");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testLike() throws Exception {

        Like l = new Like();
        UUID userUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        User liker = new User(userUUID);
        Post post = new Post(postUUID);

        l.setLiker(liker);
        l.setPost(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + apiVersion + "/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(l)))
                .andExpect(status().isOk());

        Mockito.doThrow(new UserNotFoundException(userUUID)).when(likeService).like(any(Like.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(l)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + userUUID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));

        Mockito.doThrow(new PostNotFoundException(postUUID)).when(likeService).like(any(Like.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(l)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Post with id: " + postUUID + " could not be found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void testDeleteLike() throws Exception {

        UUID userUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/like/" + userUUID + "/" + postUUID))
                .andExpect(status().isOk());

        Mockito.doThrow(new PostNotFoundException(postUUID)).when(likeService).removeLike(userUUID, postUUID);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/like/" + userUUID + "/" + postUUID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Post with id: " + postUUID + " could not be found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));

        Mockito.doThrow(new UserNotFoundException(userUUID)).when(likeService).removeLike(userUUID, postUUID);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/like/" + userUUID + "/" + postUUID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + userUUID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));

        Mockito.doThrow(new LikeNotFoundException(userUUID, postUUID)).when(likeService).removeLike(userUUID, postUUID);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/like/" + userUUID + "/" + postUUID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + userUUID + " has not liked the post with id: " + postUUID))
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
