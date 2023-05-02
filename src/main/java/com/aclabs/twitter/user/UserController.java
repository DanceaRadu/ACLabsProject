package com.aclabs.twitter.user;

import com.aclabs.twitter.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "user")
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

    @GetMapping(path = "search")
    public List<User> search(@RequestParam String searchTerm) {
        return userService.search(searchTerm);
    }

    @DeleteMapping(path = "unregister")
    public void unregister(@RequestParam String username) {
        userService.unregister(username);
    }

    @GetMapping(path = "{username}/getOwnPosts")
    public List<Post> getOwnPosts(@PathVariable String username, @RequestParam(required = false)@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date filterTime) {
        return userService.getOwnPosts(username, filterTime);
    }

    @GetMapping(path = "{username}/getFeed")
    public List<List<Post>> getFollowedPosts(@PathVariable String username) {
        return userService.getFeed(username);
    }

}
