package com.aclabs.twitter.controller;

import com.aclabs.twitter.exceptionhandling.advice.AppAdvice;
import com.aclabs.twitter.exceptionhandling.exceptions.NoQueryResultException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.mapstruct.DTO.PostGetDTO;
import com.aclabs.twitter.mapstruct.DTO.UserSearchDTO;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.UserService;
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

import java.util.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new AppAdvice()).build();
    }

    @Test
    public void registerTest() throws Exception{

        User user = new User(UUID.randomUUID());
        user.setUsername("GoNemesis");
        user.setFirstName("Radu");
        user.setLastName("Dancea");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1.1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void testSearch() throws Exception {

        UserSearchDTO user1 = new UserSearchDTO();
        user1.setUsername("GoNemesis");
        UserSearchDTO user2 = new UserSearchDTO();
        user2.setUsername("GoKart");
        List<UserSearchDTO> users = Arrays.asList(user1, user2);

        Mockito.when(userService.search("Go")).thenReturn(users);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1.1/user?query=Go").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("GoNemesis"))
                .andExpect(jsonPath("$[1].username").value("GoKart"));

        Mockito.when(userService.search("NoResult")).thenThrow(new NoQueryResultException("NoResult"));
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1.1/user?query=NoResult"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("The search using the query = 'NoResult' didn't return any results"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void testDelete() throws Exception {

        UUID userID = UUID.randomUUID();
        UUID wrongID = UUID.randomUUID();
        Mockito.doThrow(new UserNotFoundException(wrongID)).when(userService).unregister(wrongID);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/v1.1/user/" + userID))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1.1/user/" + wrongID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + wrongID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void getOwnPostsTest() throws Exception{

        UUID userUUID = UUID.randomUUID();
        UUID wrongID = UUID.randomUUID();

        PostGetDTO post1 = new PostGetDTO();
        post1.setMessage("Hello world!");

        PostGetDTO post2 = new PostGetDTO();
        post2.setMessage("Testing the getOwnPosts method");

        List<PostGetDTO> posts = Arrays.asList(post1, post2);

        Mockito.when(userService.getOwnPosts(userUUID, java.util.Optional.empty())).thenReturn(posts);
        Mockito.when(userService.getOwnPosts(wrongID, java.util.Optional.empty())).thenThrow(new UserNotFoundException(wrongID));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1.1/user/" + userUUID + "/posts")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("Hello world!"))
                .andExpect(jsonPath("$[1].message").value("Testing the getOwnPosts method"));

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/v1.1/user/" + wrongID + "/posts"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + wrongID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void getFollowedPostsTest() throws Exception {

        UUID userID = UUID.randomUUID();
        UUID wrongID = UUID.randomUUID();

        PostGetDTO post1 = new PostGetDTO();
        post1.setMessage("Hello world!");
        PostGetDTO post2 = new PostGetDTO();
        post2.setMessage("Test message");
        PostGetDTO post3 = new PostGetDTO();
        post3.setMessage("This is a message");
        PostGetDTO post4 = new PostGetDTO();
        post4.setMessage("This is a test message");

        List<List<PostGetDTO>> posts = Arrays.asList(Arrays.asList(post1, post2), Arrays.asList(post3, post4));
        Mockito.when(userService.getFeed(userID)).thenReturn(posts);
        Mockito.when(userService.getFeed(wrongID)).thenThrow(new UserNotFoundException(wrongID));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1.1/user/" + userID + "/feed")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0][0].message").value("Hello world!"))
                .andExpect(jsonPath("$[0][1].message").value("Test message"))
                .andExpect(jsonPath("$[1][0].message").value("This is a message"))
                .andExpect(jsonPath("$[1][1].message").value("This is a test message"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1.1/user/" + wrongID + "/feed"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + wrongID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    @Test
    public void getMentionsTest() throws Exception {

        UUID userID = UUID.randomUUID();
        UUID wrongID = UUID.randomUUID();

        PostGetDTO post1 = new PostGetDTO();
        post1.setMessage("GoNemesis is cool");
        PostGetDTO post2 = new PostGetDTO();
        post2.setMessage("GoNemesis sucks");

        List<PostGetDTO> posts = Arrays.asList(post1, post2);

        Mockito.when(userService.getMentions(userID)).thenReturn(posts);
        Mockito.when(userService.getMentions(wrongID)).thenThrow(new UserNotFoundException(wrongID));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1.1/user/" + userID + "/mentions")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].message").value("GoNemesis is cool"))
                .andExpect(jsonPath("$[1].message").value("GoNemesis sucks"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1.1/user/" + wrongID + "/mentions"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("error").value("User with id: " + wrongID + " was not found"))
                .andExpect(jsonPath("status").value("NOT_FOUND"));
    }

    // Utility method to convert object to JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}