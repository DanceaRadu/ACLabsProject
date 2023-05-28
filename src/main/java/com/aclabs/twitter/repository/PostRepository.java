package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> getPostByPoster_UserID(UUID posterID);
    List<Post> getPostByPoster_UserIDAndPostDateAfter(UUID posterID, Timestamp timestamp);
    List<Post> getPostByMessageContains(String mentionedUsername);
}
