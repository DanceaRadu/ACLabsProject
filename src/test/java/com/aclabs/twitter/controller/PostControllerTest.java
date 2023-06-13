package com.aclabs.twitter.controller;


import com.aclabs.twitter.exceptionhandling.advice.AppAdvice;
import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.mapstruct.DTO.PostNoDateDTO;
import com.aclabs.twitter.mapstruct.DTO.RepostDTO;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.PostService;
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

@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;
    private String apiVersion;

    @BeforeEach
    public void setup() {
        PostController postController = new PostController(postService);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).setControllerAdvice(new AppAdvice()).build();

        try (InputStream input = new FileInputStream("src/test/resources/test.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiVersion = prop.getProperty("api-version");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testPost() throws Exception {

        PostNoDateDTO post = new PostNoDateDTO();
        post.setMessage("Hello there");

        UUID userUUID = UUID.randomUUID();
        User user = new User(userUUID);
        post.setPoster(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + apiVersion + "/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(post)))
                .andExpect(status().isOk());

        Mockito.doThrow(new UserNotFoundException(userUUID)).when(postService).post(any(PostNoDateDTO.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(post)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + userUUID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void testDelete() throws Exception {

        UUID postUUID = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/" + apiVersion + "/post/" + postUUID))
                .andExpect(status().isOk());

        Mockito.doThrow(new PostNotFoundException(postUUID)).when(postService).deletePost(postUUID);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/post/" + postUUID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("Post with id: " + postUUID + " could not be found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void testRepost() throws Exception {

        RepostDTO repost = new RepostDTO();

        UUID reposterUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        repost.setReposterID(reposterUUID);
        repost.setPostID(postUUID);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + apiVersion + "/post/repost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(repost)))
                .andExpect(status().isOk());

        Mockito.doThrow(new UserNotFoundException(reposterUUID)).when(postService).repost(any(RepostDTO.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/post/repost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(repost)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + reposterUUID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));

        Mockito.doThrow(new PostNotFoundException(postUUID)).when(postService).repost(any(RepostDTO.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/post/repost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(repost)))
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