package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.LikeNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Like;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.repository.LikeRepository;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository = Mockito.mock(LikeRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    private final LikeService likeService = new LikeService(likeRepository, userRepository, postRepository);

    @Test
    void testLike() {

        //test exceptions
        UUID likerUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        Like.LikeID likeID = new Like.LikeID(likerUUID, postUUID);

        Like l = new Like();
        l.setLikeID(likeID);
        l.setPost(new Post(postUUID));
        l.setLiker(new User(likerUUID));

        Mockito.when(postRepository.existsById(postUUID)).thenReturn(false);
        assertThrows(PostNotFoundException.class, () -> likeService.like(l));
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(true);
        Mockito.when(userRepository.existsById(likerUUID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> likeService.like(l));

        //test normal addition
        Mockito.when(userRepository.existsById(likerUUID)).thenReturn(true);
        assertDoesNotThrow(() -> likeService.like(l));
    }

    @Test
    void removeLike() {
        //test exceptions
        UUID likerUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        Like.LikeID likeID = new Like.LikeID(likerUUID, postUUID);

        Like l = new Like();
        l.setLikeID(likeID);
        l.setPost(new Post(postUUID));
        l.setLiker(new User(likerUUID));

        Mockito.when(postRepository.existsById(postUUID)).thenReturn(false);
        assertThrows(PostNotFoundException.class, () -> likeService.removeLike(likerUUID, postUUID));
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(true);
        Mockito.when(userRepository.existsById(likerUUID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> likeService.removeLike(likerUUID, postUUID));
        Mockito.when(userRepository.existsById(likerUUID)).thenReturn(true);

        Mockito.when(likeRepository.existsById(likeID)).thenReturn(false);
        assertThrows(LikeNotFoundException.class, () -> likeService.removeLike(likerUUID, postUUID));

        //test normal addition
        Mockito.when(likeRepository.existsById(likeID)).thenReturn(true);
        assertDoesNotThrow(() -> likeService.removeLike(likerUUID, postUUID));
    }
}