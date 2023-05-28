package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.NoQueryResultException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.mapstruct.DTO.PostGetDTO;
import com.aclabs.twitter.mapstruct.DTO.ReplyGetDTO;
import com.aclabs.twitter.mapstruct.DTO.UserSearchDTO;
import com.aclabs.twitter.mapstruct.mappers.DTOMapper;
import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DTOMapper DTOMapper;

    @Autowired
    public UserService(UserRepository userRepository, PostRepository postRepository, BCryptPasswordEncoder passwordEncoder, DTOMapper DTOMapper) {
        this.userRepository =  userRepository;
        this.passwordEncoder = passwordEncoder;
        this.DTOMapper = DTOMapper;
        this.postRepository = postRepository;
    }

    public void register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    public List<UserSearchDTO> search(String searchTerm) {
        List<User> list =  userRepository.findUsersByUsernameContainsOrFirstNameContainsOrLastNameContains(searchTerm, searchTerm, searchTerm);
        if(list.isEmpty()) throw new NoQueryResultException(searchTerm);

        return list.stream().map(DTOMapper::userToUserSearchDTO).toList();
    }

    public void unregister(UUID userID) {
        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        userRepository.deleteById(userID);
    }

    public List<PostGetDTO> getOwnPosts(UUID userID, Optional<Date> filterTime) {
        List<Post> postList;
        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        if(filterTime.isPresent())
            postList = postRepository.getPostByPoster_UserIDAndPostDateAfter(userID, new Timestamp(filterTime.get().getTime()));
        else
            postList = postRepository.getPostByPoster_UserID(userID);
        return postList.stream().map(DTOMapper::postToPostGetDTO).toList();
    }

    public List<List<PostGetDTO>> getFeed(UUID userID) {
        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        List<List<PostGetDTO>> list =  userRepository
                .getReferenceById(userID)
                .getFollows().stream()
                .map(Follow::getFollowedUser)
                .map(e -> e.getPosts().stream().map(DTOMapper::postToPostGetDTO).toList()
        ).toList();

        for(List<PostGetDTO> postList: list )
            for(PostGetDTO post : postList)
                post.setReplies(post.getReplies().stream().filter(ReplyGetDTO::isPublic).toList());

        return list;
    }

    public List<PostGetDTO> getMentions(UUID userID) {
        if(!userRepository.existsById(userID)) throw new UserNotFoundException(userID);
        String username = userRepository.getReferenceById(userID).getUsername();
        return postRepository.getPostByMessageContains(username).stream().map(DTOMapper::postToPostGetDTO).toList();
    }
}
