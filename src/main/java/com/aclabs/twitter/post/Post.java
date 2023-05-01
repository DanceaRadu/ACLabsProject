package com.aclabs.twitter.post;

import com.aclabs.twitter.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="posts")
public class Post {
    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "poster_username", referencedColumnName = "username")
    private User poster;

    @Column(name = "message", nullable = false)
    private String message;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getPosterUsername() {
        return poster.getUsername();
    }

    public void setPoster(User user){
        this.poster = user;
    }

    public User getPoster() {
        return poster;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage() {
        this.message = message;
    }
}
