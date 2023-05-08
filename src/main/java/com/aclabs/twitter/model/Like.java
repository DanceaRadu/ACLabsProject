package com.aclabs.twitter.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "likes")
public @Data class Like {

    @Embeddable
    public static class LikeID implements Serializable{

        @Column(name = "liker_id", nullable = false)
        private Long likerID;
        @Column(name = "post_id", nullable = false)
        private Long postID;

        public LikeID() {

        }

        public LikeID(Long likerUsername, Long postID) {
            this.likerID = likerUsername;
            this.postID = postID;
        }

        public Long getLikerUsername() {
            return likerID; }
        public void setLikerUsername(Long likerID) {
            this.likerID= likerID;
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
                return id.likerID == this.likerID && id.postID == this.postID;
            }
            return false;
        }
   }

    @EmbeddedId
    private LikeID likeID = new LikeID();

    @JsonBackReference(value = "Post Likes")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable=false, updatable=false)
    Post post;

    @JsonBackReference(value = "User likes")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id", insertable=false, updatable=false)
    User liker;
}
