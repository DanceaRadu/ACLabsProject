package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.Post;
import com.aclabs.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findUsersByUsernameContainsOrFirstNameContainsOrLastNameContains(String query, String query1, String query2);
}
