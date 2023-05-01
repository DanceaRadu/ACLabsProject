package com.aclabs.twitter.user;

import com.aclabs.twitter.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public ArrayList<User> search(@RequestParam String searchTerm) {
        return userService.search(searchTerm);
    }

    @DeleteMapping(path = "unregister")
    public void unregister(@RequestParam String username) {
        userService.unregister(username);
    }

    @GetMapping(path ="allusers")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
