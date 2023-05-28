package com.aclabs.twitter.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "replies")
public @Data class Reply {

    @Id
    @Column(name = "reply_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID replyID;

    @Column(name = "reply_text", nullable = false)
    private String replyText;

    @Column(name = "public", nullable = false)
    private boolean isPublic = true;

    @Column(name = "reply_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp postDate;

    @JsonBackReference(value = "Post replies")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_post_id", referencedColumnName = "post_id")
    private Post parentPost;

    @JsonBackReference(value = "Replier")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User replier;
}
