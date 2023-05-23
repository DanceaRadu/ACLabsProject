package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.NoQueryResultException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Follow;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository =  userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(User user) {
        System.out.println(user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<User> search(String searchTerm) {
        List<User> list =  userRepository.findUsersByUsernameContainsOrFirstNameContainsOrLastNameContains(searchTerm, searchTerm, searchTerm);
        if(list.isEmpty()) throw new NoQueryResultException(searchTerm);
        else return list;
    }

    public void unregister(UUID userID) {
        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        userRepository.deleteById(userID);
    }

    public List<Post> getOwnPosts(UUID userID, Date filterTime) {

        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        if(filterTime != null)
            return userRepository.getReferenceById(userID).getPosts().stream().filter(e -> e.getPostDate().after(filterTime)).toList();
        else
            return userRepository.getReferenceById(userID).getPosts();
    }

    public List<List<Post>> getFeed(UUID userID) {

        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        return userRepository.getReferenceById(userID).getFollows().stream().map(Follow::getFollowedUser).map(User::getPosts).toList();
    }
}
