package com.aclabs.twitter.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;


@Entity
@Table(name = "likes")
public class Like {

    public Like() {

    }

    @Embeddable
    public static class LikeID implements Serializable{

        @Column(name = "liker_username", nullable = false)
        private String likerUsername;
        @Column(name = "post_id", nullable = false)
        private Long postID;

        public LikeID() {

        }

        public LikeID(String likerUsername, Long postID) {
            this.likerUsername = likerUsername;
            this.postID = postID;
        }

        public String getLikerUsername() {
            return likerUsername;
        }

        public void setLikerUsername(String likerUsername) {
            this.likerUsername = likerUsername;
        }

        public Long getPostID() {
            return postID;
        }

        public void setPostID(Long postID) {
            this.postID = postID;
        }

        @Override
        public boolean equals(Object o) {
            if(o instanceof LikeID id) {
                return id.likerUsername.equals(this.likerUsername)&& id.postID == this.postID;
            }
            return false;
        }
   }

    public Like(LikeID likeID) {
        this.likeID = likeID;
    }

    @EmbeddedId
    private LikeID likeID = new LikeID();

    @JsonBackReference(value = "postLikes")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable=false, updatable=false)
    Post post;

    @JsonBackReference(value = "userLikeReference")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_username", insertable=false, updatable=false)
    User liker;

    public LikeID getLikeID() {
        return likeID;
    }

    public void setLikeID(LikeID likeID) {
        this.likeID = likeID;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getLiker() {
        return liker;
    }

    public void setLiker(User liker) {
        this.liker = liker;
    }
}
