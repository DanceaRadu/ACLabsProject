package com.aclabs.twitter.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="users")
public @Data class User {

    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userID;
    @Column(name="username", nullable = false, unique = true)
    private String username;
    @Column(name="first_name",  nullable=false)
    private String firstName;
    @Column(name="last_name", nullable=false)
    private  String lastName;
    @Column(name="email", nullable=false)
    private String email;
    @Column(name="password", length=100, nullable=false)
    private String password;

    @JsonManagedReference (value = "User posts")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poster", cascade = CascadeType.REMOVE)
    private List<Post> posts;

    @JsonManagedReference (value = "User followers")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followerUser", cascade = CascadeType.REMOVE)
    private List<Follow> follows;

    @JsonManagedReference (value = "Followed users")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "followedUser", cascade = CascadeType.REMOVE)
    private List<Follow> followers;

    @JsonManagedReference (value = "User likes")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "liker", cascade = CascadeType.REMOVE)
    private List<Like> likedPosts;

    @JsonManagedReference (value = "Replier")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "replier", cascade = CascadeType.REMOVE)
    private List<Reply> replies;

    public User(UUID userID) {
        this.userID = userID;
    }
    public User() {}
}
