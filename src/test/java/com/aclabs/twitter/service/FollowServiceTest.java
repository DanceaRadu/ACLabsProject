package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.FollowRelationNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.InvalidFollowException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.repository.FollowRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class FollowServiceTest {

    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private FollowRepository followRepository = Mockito.mock(FollowRepository.class);
    private final FollowService followService = new FollowService(followRepository, userRepository);

    @Test
    void followTest() {
        UUID user1UUID = UUID.randomUUID();
        UUID user2UUID = UUID.randomUUID();
        User user1 = new User(user1UUID);
        User user2 = new User(user2UUID);

        Mockito.when(userRepository.existsById(user1UUID)).thenReturn(false);
        Mockito.when(userRepository.existsById(user2UUID)).thenReturn(false);

        Follow f = new Follow();
        f.setFollowedUser(user1);
        f.setFollowerUser(user2);

        assertThrows(UserNotFoundException.class, () -> followService.follow(f));
        Mockito.when(userRepository.existsById(user1UUID)).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> followService.follow(f));
        Mockito.when(userRepository.existsById(user2UUID)).thenReturn(true);
        user2.setUserID(user1UUID);
        assertThrows(InvalidFollowException.class, () -> followService.follow(f));
        user2.setUserID(user2UUID);

        assertDoesNotThrow(() -> followService.follow(f));
    }

    @Test
    void unfollowTest() {
        UUID user1UUID = UUID.randomUUID();
        UUID user2UUID = UUID.randomUUID();
        User user1 = new User(user1UUID);
        User user2 = new User(user2UUID);

        Follow f = new Follow();
        f.setFollowID(UUID.randomUUID());
        f.setFollowedUser(user1);
        f.setFollowerUser(user2);

        Mockito.when(followRepository.getFollowByFollowerUser_UserIDAndFollowedUser_UserID(user1UUID, user2UUID)).thenReturn(java.util.Optional.empty());
        assertThrows(FollowRelationNotFoundException.class, () -> followService.unfollow(user1UUID, user2UUID));
        Mockito.when(followRepository.getFollowByFollowerUser_UserIDAndFollowedUser_UserID(user1UUID, user2UUID)).thenReturn(java.util.Optional.of(f));
        assertDoesNotThrow(() -> followService.unfollow(user1UUID, user2UUID));
    }
}