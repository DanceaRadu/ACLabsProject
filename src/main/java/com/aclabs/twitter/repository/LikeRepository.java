package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Like.LikeID> {

}
