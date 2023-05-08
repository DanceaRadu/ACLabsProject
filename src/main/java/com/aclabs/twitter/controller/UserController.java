package com.aclabs.twitter.controller;

import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "register")
    public void register(@RequestBody User user) {
        userService.register(user);
    }

    @GetMapping
    public List<User> search(@RequestParam String searchTerm) {
        return userService.search(searchTerm);
    }

    @DeleteMapping
    public void unregister(@RequestParam Long userID) {
        userService.unregister(userID);
    }

    @GetMapping(path = "{userID}/posts")
    public List<Post> getOwnPosts(@PathVariable Long userID, @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date filterTime) {
        return userService.getOwnPosts(userID, filterTime);
    }

    @GetMapping(path = "{userID}/feed")
    public List<List<Post>> getFollowedPosts(@PathVariable Long userID) {
        return userService.getFeed(userID);
    }

}
