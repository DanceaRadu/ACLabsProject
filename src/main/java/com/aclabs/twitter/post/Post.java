package com.aclabs.twitter.post;

import com.aclabs.twitter.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name="posts")
public class Post {

    @Id
    @Column(name="post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "post")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_username", referencedColumnName = "username")
    private User poster;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "post_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp postDate;

    public Post(Long id, User poster, String message, Timestamp postDate) {
        this.id = id;
        this.poster = poster;
        this.message = message;
        this.postDate = postDate;
    }

    public Post(Long id) {
        this.id = id;
    }

    public Post() {}

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setPoster(User user){
        this.poster = user;
    }

    public User getPoster() {
        return poster;
    }

    public void setMessage() {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setPostDate(Timestamp postDate) { this.postDate = postDate; }

    public Timestamp getPostDate() { return postDate; }
}
