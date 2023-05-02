package com.aclabs.twitter.follow;

import com.aclabs.twitter.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "follows")
public class Follow {

    @Id
    @Column(name = "follow_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long followId;

    @JsonBackReference(value = "followerReference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_username", referencedColumnName = "username")
    private User follower;

    @JsonBackReference(value = "followedReference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_username", referencedColumnName = "username")
    private User followed;

    public Follow(Long follow_id, User follower, User followed) {
        this.followId = follow_id;
        this.followed = followed;
        this.follower = follower;
    }

    public Follow() {}

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowed() {
        return followed;
    }

    public void setFollowed(User followed) {
        this.followed = followed;
    }
}
