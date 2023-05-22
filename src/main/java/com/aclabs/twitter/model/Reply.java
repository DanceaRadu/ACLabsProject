package com.aclabs.twitter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Table(name = "replies")
public @Data class Reply {

    @Id
    @Column(name = "reply_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyID;

    @Column(name = "reply_text", nullable = false)
    private String replyText;

    @Column(name = "public", nullable = false)
    private boolean isPublic;

    @Column(name = "reply_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp postDate;

    @JsonBackReference(value = "Post replies")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_post_id", referencedColumnName = "post_id")
    private Post parentPost;
}
