package com.aclabs.twitter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="posts")
public @Data class Post {

    @Id
    @Column(name="post_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JsonBackReference(value = "User posts")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "poster_id", referencedColumnName = "user_id", nullable = false)
    private User poster;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "post_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Europe/Bucharest")
    private Timestamp postDate;

    @JsonManagedReference(value = "Post Likes")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Like> postLikes;

    @JsonManagedReference(value = "Post replies")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentPost", cascade = CascadeType.REMOVE)
    private List<Reply> replies;

    public Post(UUID id) {
        this.id =  id;
    }
    public Post() {}
}
