package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.PostNotFoundException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.mapstruct.DTO.PostNoDateDTO;
import com.aclabs.twitter.mapstruct.DTO.RepostDTO;
import com.aclabs.twitter.mapstruct.mappers.DTOMapper;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class PostServiceTest {

    @Mock
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private DTOMapper mapper = Mockito.mock(DTOMapper.class);
    private final PostService postService = new PostService(postRepository, userRepository, mapper);

    @Test
    public void testPost() {

        //Test when the user UUID is not valid
        UUID uuid = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        User user = new User(uuid);
        PostNoDateDTO postDTO = new PostNoDateDTO();
        postDTO.setPoster(user);
        postDTO.setId(postUUID);

        Mockito.when(userRepository.existsById(uuid)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> postService.post(postDTO));

        //test normal addition
        Mockito.when(userRepository.existsById(uuid)).thenReturn(true);
        Post post = new Post();
        post.setPoster(user);
        post.setId(postUUID);
        Mockito.when(mapper.postNoDateDTOToPost(postDTO)).thenReturn(post);

        assertDoesNotThrow(() -> postService.post(postDTO));
        assertNotNull(post.getPostDate());

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(post.getPostDate().getTime());
        int year1 = cal.get(Calendar.YEAR);
        cal.setTimeInMillis(new Date().getTime());
        int year2 = cal.get(Calendar.YEAR);

        assertEquals(year1, year2, "The years of the post date and the current date should be the same");
    }

    @Test
    void testDeletion() {
        UUID postUUID = UUID.randomUUID();
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(false);
        assertThrows(PostNotFoundException.class, () -> postService.deletePost(postUUID));
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(true);
        assertDoesNotThrow(() -> postService.deletePost(postUUID));
    }

    @Test
    void testRepost() {
        UUID reposterUUID = UUID.randomUUID();
        UUID postUUID = UUID.randomUUID();
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(false);
        Mockito.when(userRepository.existsById(reposterUUID)).thenReturn(false);

        RepostDTO repost = new RepostDTO();
        repost.setReposterID(reposterUUID);
        repost.setPostID(postUUID);

        assertThrows(PostNotFoundException.class, () -> postService.repost(repost));
        Mockito.when(postRepository.existsById(postUUID)).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> postService.repost(repost));
        Mockito.when(userRepository.existsById(reposterUUID)).thenReturn(true);

        Post post = new Post(postUUID);
        post.setPostDate(new Timestamp(new Date().getTime()));
        Mockito.when(postRepository.getReferenceById(postUUID)).thenReturn(post);
        assertDoesNotThrow(() -> postService.repost(repost));
    }
}