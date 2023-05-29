package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.ReplyRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class ReplyServiceTest {

    @Mock
    private ReplyRepository replyRepository = Mockito.mock(ReplyRepository.class);
    @Mock
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private final ReplyService replyService = new ReplyService(replyRepository, postRepository, userRepository);

    @Test
    void testAddReply() {

        UUID postUUID = UUID.randomUUID();
        UUID userUUID = UUID.randomUUID();

        User user = new User(userUUID);
        Post post = new Post(postUUID);
        Reply reply = new Reply();
        reply.setReplier(user);
        reply.setParentPost(post);

        Mockito.when(postRepository.existsById(postUUID)).thenReturn(false);
        assertThrows(PostNotFoundException.class, () -> replyService.addReply(reply));
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(true);
        Mockito.when(userRepository.existsById(userUUID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> replyService.addReply(reply));

        Mockito.when(userRepository.existsById(userUUID)).thenReturn(true);
        assertDoesNotThrow(() -> replyService.addReply(reply));
    }
}