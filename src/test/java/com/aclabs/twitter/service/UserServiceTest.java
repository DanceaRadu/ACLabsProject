package com.aclabs.twitter.service;

import com.aclabs.twitter.exceptionhandling.exceptions.NoQueryResultException;
import com.aclabs.twitter.exceptionhandling.exceptions.UserNotFoundException;
import com.aclabs.twitter.mapstruct.DTO.PostGetDTO;
import com.aclabs.twitter.mapstruct.DTO.ReplyGetDTO;
import com.aclabs.twitter.mapstruct.DTO.UserSearchDTO;
import com.aclabs.twitter.mapstruct.mappers.DTOMapper;
import com.aclabs.twitter.model.Follow;
import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.Reply;
import com.aclabs.twitter.model.User;
import com.aclabs.twitter.repository.PostRepository;
import com.aclabs.twitter.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    @Mock
    private PostRepository postRepository = Mockito.mock(PostRepository.class);
    @Mock
    private DTOMapper mapper = Mockito.mock(DTOMapper.class);
    @Mock
    private final BCryptPasswordEncoder encoder = Mockito.mock(BCryptPasswordEncoder.class);
    private final UserService userService = new UserService(userRepository, postRepository, encoder, mapper);

    private User createUser(UUID uuid, String username, String firstName, String lastName) {
        User user = new User(uuid);
        user.setFirstName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail("email@email.com");
        return user;
    }

    private UserSearchDTO createUserSearchDTO(String username, String firstName, String lastName) {
        UserSearchDTO user = new UserSearchDTO();
        user.setFirstName(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail("email@email.com");
        return user;
    }


    @Test
    void testRegister() {
        User newUser = new User();
        newUser.setPassword("password");

        Mockito.when(encoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        userService.register(newUser);
        assertEquals(newUser, userRepository.save(newUser));
        assertEquals("encodedPassword", newUser.getPassword(), "User password should be encoded");
    }

    @Test
    void testUnregister() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(userRepository.existsById(uuid)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.unregister(uuid));

        Mockito.when(userRepository.existsById(uuid)).thenReturn(true);
        assertDoesNotThrow(() -> userService.unregister(uuid));
    }

    @Test
    void testGetOwnPosts() {

        //test wrong UUID provided
        UUID userUUID = UUID.randomUUID();
        Mockito.when(userRepository.existsById(userUUID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getOwnPosts(userUUID, java.util.Optional.empty()));

        Post post1 = new Post(new UUID(100, 100));
        post1.setMessage("Message1");
        Post post2 = new Post(new UUID(150, 150));
        post2.setMessage("Message2");

        PostGetDTO postGetDTO1 = new PostGetDTO();
        postGetDTO1.setMessage("Message1");
        PostGetDTO postGetDTO2 = new PostGetDTO();
        postGetDTO2.setMessage("Message2");

        List<Post> postList = List.of(post1, post2);
        List<PostGetDTO> postGetDTOList = List.of(postGetDTO1, postGetDTO2);

        Mockito.when(userRepository.existsById(userUUID)).thenReturn(true);
        Mockito.when(mapper.postToPostGetDTO(post1)).thenReturn(postGetDTO1);
        Mockito.when(mapper.postToPostGetDTO(post2)).thenReturn(postGetDTO2);
        Mockito.when(postRepository.getPostByPoster_UserID(userUUID)).thenReturn(postList);
        Mockito.when(postRepository.getPostByPoster_UserIDAndPostDateAfter(userUUID, new Timestamp(20))).thenReturn(postList);

        assertEquals(postGetDTOList, userService.getOwnPosts(userUUID, java.util.Optional.empty()));
        assertEquals(postGetDTOList, userService.getOwnPosts(userUUID, Optional.of(new Timestamp(20))));
    }

    @Test
    void testSearch() {

        User user1 = createUser(new UUID(100,100), "GoNemesis", "Radu", "Dancea");
        User user2 = createUser(new UUID(100,100), "Rad", "Ana", "Popescu");
        User user3 = createUser(new UUID(100,100), "ComputerGuy", "Mihai", "Ionescu");

        UserSearchDTO user1DTO = createUserSearchDTO("GoNemesis", "Radu", "Dancea");
        UserSearchDTO user2DTO = createUserSearchDTO("Rad", "Ana", "Popescu");
        UserSearchDTO user3DTO = createUserSearchDTO("ComputerGuy", "Mihai", "Ionescu");

        Mockito.when(mapper.userToUserSearchDTO(user1)).thenReturn(user1DTO);
        Mockito.when(mapper.userToUserSearchDTO(user2)).thenReturn(user2DTO);
        Mockito.when(mapper.userToUserSearchDTO(user3)).thenReturn(user3DTO);

        List<UserSearchDTO> dtoList = List.of(mapper.userToUserSearchDTO(user1), mapper.userToUserSearchDTO(user2));
        List<User> userList = List.of(user1, user2);

        Mockito
                .doReturn(userList)
                .when(userRepository)
                .findUsersByUsernameContainsOrFirstNameContainsOrLastNameContains("Radu", "Radu", "Radu");
        Mockito
                .doReturn(new ArrayList<User>())
                .when(userRepository)
                .findUsersByUsernameContainsOrFirstNameContainsOrLastNameContains("Magazin", "Magazin", "Magazin");

        List<UserSearchDTO> returnedList = userService.search("Radu");
        assertEquals(dtoList, returnedList);
        assertThrows(NoQueryResultException.class , () -> userService.search("Magazin"));
    }

    @Test
    void testGetFeed() {

        //Test Wrong UUID provided
        UUID userUUID = UUID.randomUUID();
        Mockito.when(userRepository.existsById(userUUID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getFeed(userUUID));

        //Create the two necessary users and make the first one follow the second one
        User follower = new User(userUUID);
        User followed = new User(UUID.randomUUID());
        Follow follow = new Follow();
        follow.setFollowerUser(follower);
        follow.setFollowedUser(followed);
        follower.setFollows(List.of(follow));

        //Add two posts (with replies) to the second, followed, user
        Post post1 = new Post();
        Post post2 = new Post();

        Reply reply = new Reply();
        Reply reply2 = new Reply();
        reply.setPublic(false);
        reply2.setPublic(true);

        ReplyGetDTO replyGetDTO1 = new ReplyGetDTO();
        ReplyGetDTO replyGetDTO2 = new ReplyGetDTO();
        replyGetDTO1.setPublic(false);
        replyGetDTO2.setPublic(true);

        post1.setMessage("Message");
        post2.setMessage("Message2");
        post1.setReplies(List.of(reply, reply2));
        post2.setReplies(List.of(reply, reply2));
        followed.setPosts(List.of(post1, post2));

        PostGetDTO postGetDTO1 = new PostGetDTO();
        PostGetDTO postGetDTO2 = new PostGetDTO();
        postGetDTO1.setMessage("Message");
        postGetDTO1.setMessage("Message2");
        postGetDTO1.setReplies(List.of(replyGetDTO1, replyGetDTO2));
        postGetDTO2.setReplies(List.of(replyGetDTO1, replyGetDTO2));

        //Mock the behaviour of the mapper and of the userRepository
        Mockito.when(userRepository.existsById(userUUID)).thenReturn(true);
        Mockito.when(userRepository.getReferenceById(userUUID)).thenReturn(follower);
        Mockito.when(mapper.postToPostGetDTO(post1)).thenReturn(postGetDTO1);
        Mockito.when(mapper.postToPostGetDTO(post2)).thenReturn(postGetDTO2);

        //assert that the posts returned are the right ones
        assertEquals(List.of(List.of(postGetDTO1, postGetDTO2)), userService.getFeed(userUUID));
        //assert that the posts returned do not contain private replies
        assertEquals(List.of(replyGetDTO2), userService.getFeed(userUUID).get(0).get(0).getReplies());
    }

    @Test
    void testMentions() {

        //test nonexistent UUID provided
        UUID userUUID = UUID.randomUUID();
        Mockito.when(userRepository.existsById(userUUID)).thenReturn(false);
        assertThrows(UserNotFoundException.class, () -> userService.getMentions(userUUID));

        //Create the posts and the PostDTO's
        Post post1 = new Post(new UUID(100, 100));
        post1.setMessage("GoNemesis is cool");
        Post post2 = new Post(new UUID(150, 150));
        post2.setMessage("GoNemesis is awesome");

        PostGetDTO postGetDTO1 = new PostGetDTO();
        postGetDTO1.setMessage("GoNemesis is cool");
        PostGetDTO postGetDTO2 = new PostGetDTO();
        postGetDTO2.setMessage("GoNemesis is awesome");

        List<Post> postList = List.of(post1, post2);
        List<PostGetDTO> postGetDTOList = List.of(postGetDTO1, postGetDTO2);

        User user = new User(userUUID);
        user.setUsername("GoNemesis");

        Mockito.when(userRepository.existsById(userUUID)).thenReturn(true);
        Mockito.when(userRepository.getReferenceById(userUUID)).thenReturn(user);
        Mockito.when(mapper.postToPostGetDTO(post1)).thenReturn(postGetDTO1);
        Mockito.when(mapper.postToPostGetDTO(post2)).thenReturn(postGetDTO2);
        Mockito.when(postRepository.getPostByMessageContains("GoNemesis")).thenReturn(postList);

        assertEquals(postGetDTOList, userService.getMentions(userUUID));
    }
}
