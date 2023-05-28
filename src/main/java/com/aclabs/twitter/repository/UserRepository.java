package com.aclabs.twitter.repository;

import com.aclabs.twitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    List<User> findUsersByUsernameContainsOrFirstNameContainsOrLastNameContains(String query, String query1, String query2);
}
