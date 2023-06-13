package com.aclabs.twitter.controller;

import com.aclabs.twitter.exceptionhandling.advice.AppAdvice;
import com.aclabs.twitter.exceptionhandling.exceptions.FollowRelationNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.InvalidFollowException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.FollowService;
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

@WebMvcTest(FollowController.class)
public class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FollowService followService;
    private String apiVersion;

    @BeforeEach
    public void setup() {
        FollowController followController = new FollowController(followService);
        mockMvc = MockMvcBuilders.standaloneSetup(followController).setControllerAdvice(new AppAdvice()).build();

        try (InputStream input = new FileInputStream("src/test/resources/test.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            apiVersion = prop.getProperty("api-version");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void followTest() throws Exception {
        Follow f = new Follow();

        UUID user1ID = UUID.randomUUID();
        UUID user2ID = UUID.randomUUID();
        User user1 = new User(user1ID);
        User user2 = new User(user2ID);

        f.setFollowedUser(user1);
        f.setFollowerUser(user2);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/" + apiVersion + "/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(f)))
                .andExpect(status().isOk());

        Mockito.doThrow(new UserNotFoundException(user1ID)).when(followService).follow(any(Follow.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(f)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + user1ID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));

        Mockito.doThrow(new InvalidFollowException()).when(followService).follow(any(Follow.class));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/" + apiVersion + "/follow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(f)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error").value("A user cannot follow themselves"))
                .andExpect(jsonPath("status").value("BAD_REQUEST"));
    }

    @Test
    public void unfollowTest() throws Exception {

        UUID followerUUID = UUID.randomUUID();
        UUID followedUUID = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/follow/" + followerUUID + "/" + followedUUID))
                .andExpect(status().isOk());

        Mockito.doThrow(new FollowRelationNotFoundException(followerUUID, followedUUID)).when(followService).unfollow(followerUUID, followedUUID);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/" + apiVersion + "/follow/" + followerUUID + "/" + followedUUID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("There is no follow relation between follower with id: " + followerUUID + " and user with id: " + followedUUID))
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
