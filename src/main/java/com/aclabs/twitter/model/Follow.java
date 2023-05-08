package com.aclabs.twitter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "follows")
public @Data class Follow {

    @Id
    @Column(name = "follow_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followID;

    @JsonBackReference(value = "User followers")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", referencedColumnName = "user_id")
    private User followerUser;

    @JsonBackReference(value = "Followed users")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", referencedColumnName = "user_id")
    private User followedUser;
}
