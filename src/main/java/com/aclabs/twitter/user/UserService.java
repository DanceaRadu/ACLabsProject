package com.aclabs.twitter.user;

import com.aclabs.twitter.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository =  userRepository;
    }

    public void register(User user) {
        userRepository.save(user);
    }

    public ArrayList<User> search(String searchTerm) {
        return new ArrayList<>();
    }

    public void unregister(String username) {
        userRepository.deleteById(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
