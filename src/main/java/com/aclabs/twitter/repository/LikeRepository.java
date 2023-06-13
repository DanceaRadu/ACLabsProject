package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Like.LikeID> {
    Optional<Like> getLikeByLikeID(Like.LikeID likeID);
}
