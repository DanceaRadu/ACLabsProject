package com.aclabs.twitter.user;

import com.aclabs.twitter.follow.FollowRepository;
import com.aclabs.twitter.post.Post;
import org.springframework.beans.factory.annotation.Autowired;
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
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<User> search(String searchTerm) {
        return userRepository.findAll().stream().filter(e -> e.getFirstName().equals(searchTerm) || e.getLastName().equals(searchTerm) || e.getUsername().equals(searchTerm)).toList();
    }

    public void unregister(String username) {
        userRepository.deleteById(username);
    }

    public List<Post> getOwnPosts(String username, Date filterTime) {
        if(filterTime != null)
            return userRepository.getReferenceById(username).getPosts().stream().filter(e -> e.getPostDate().after(filterTime)).toList();
        else
            return userRepository.getReferenceById(username).getPosts();
    }

    public List<List<Post>> getFeed(String username) {
        return followRepository.findAll().stream().filter(e -> e.getFollower().getUsername().equals(username)).map(e -> e.getFollowed().getPosts()).toList();
    }

}
