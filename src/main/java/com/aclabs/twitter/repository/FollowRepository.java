package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> getFollowByFollowerUser_UserIDAndFollowedUser_UserID(Long followerID, Long followedID);
}
