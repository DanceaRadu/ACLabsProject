package com.aclabs.twitter.controller;

import com.aclabs.twitter.mapstruct.DTO.PostGetDTO;
import com.aclabs.twitter.mapstruct.DTO.UserSearchDTO;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1.1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user and add him to the database")
    @PostMapping(path = "register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @Operation(summary = "Search users by their last names, first names or usernames",
            responses = {
                    @ApiResponse(description = "The user",
                        content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = UserSearchDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No users were found")})
    @GetMapping
    public List<UserSearchDTO> search(@RequestParam String query) {
       return userService.search(query);
    }
    @Operation(summary = "Delete a user with a given ID from the database",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User was deleted"),
                    @ApiResponse(responseCode = "404", description = "No user was found for the given id")})
    @DeleteMapping(path = "{userID}")
    public void unregister(@PathVariable UUID userID) {
        userService.unregister(userID);
    }

    @Operation(summary = "Get all of the posts of a user based on his ID",
            responses = {
                    @ApiResponse(description = "The posts",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostGetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No user was found for the given id")})
    @GetMapping(path = "{userID}/posts")
    public List<PostGetDTO> getOwnPosts(@PathVariable UUID userID, @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Optional<Date> filterTime) {
        return userService.getOwnPosts(userID, filterTime);
    }

    @Operation(summary = "Get a user's feed based on his ID",
            responses = {
                    @ApiResponse(description = "The posts",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = PostGetDTO.class))),
                    @ApiResponse(responseCode = "404", description = "No user was found for the given id")})
    @GetMapping(path = "{userID}/feed")
    public List<List<PostGetDTO>> getFollowedPosts(@PathVariable UUID userID) {
        return userService.getFeed(userID);
    }

    @Operation(summary = "Get the posts in which a user was mentioned",
            responses = {
                @ApiResponse(description = "The posts",
                        content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PostGetDTO.class))),
                @ApiResponse(responseCode = "404", description = "No user was found for the given id")})
    @GetMapping(path = "{userID}/mentions")
    public List<PostGetDTO> getMentions(@PathVariable UUID userID) {
        return userService.getMentions(userID);
    }
}
