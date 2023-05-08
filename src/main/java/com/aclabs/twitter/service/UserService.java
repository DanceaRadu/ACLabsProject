package com.aclabs.twitter.service;

import com.aclabs.twitter.model.User;
import com.aclabs.twitter.repository.FollowRepository;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, FollowRepository followRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository =  userRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        System.out.println(user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<User> search(String searchTerm) {
        return userRepository.findAll().stream().filter(e -> e.getFirstName().equals(searchTerm) || e.getLastName().equals(searchTerm) || e.getUsername().equals(searchTerm)).toList();
    }

    public void unregister(Long userID) {
        userRepository.deleteById(userID);
    }

    public List<Post> getOwnPosts(Long userID, Date filterTime) {
        if(filterTime != null)
            return userRepository.getReferenceById(userID).getPosts().stream().filter(e -> e.getPostDate().after(filterTime)).toList();
        else
            return userRepository.getReferenceById(userID).getPosts();
    }

    public List<List<Post>> getFeed(Long userID) {
        return followRepository.findAll().stream().filter(e -> e.getFollowerUser().getUserID().equals(userID)).map(e -> e.getFollowedUser().getPosts()).toList();
    }
}
