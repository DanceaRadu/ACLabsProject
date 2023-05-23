package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {
    Optional<Follow> getFollowByFollowerUser_UserIDAndFollowedUser_UserID(UUID followerID, UUID followedID);
}
