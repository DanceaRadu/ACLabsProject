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

        public LikeID(Long likerID, Long postID) {
            this.likerID = likerID;
            this.postID = postID;
        }

        public Long getLikerID() {
            return likerID; }
        public void setLikerID(Long likerID) {
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

        @Override
        public int hashCode(){
            return (int)(likerID + postID);
        }
   }

    @EmbeddedId
    private LikeID likeID = new LikeID();

    @JsonBackReference(value = "Post Likes")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable=false, updatable=false)
    private Post post;

    @JsonBackReference(value = "User likes")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liker_id", insertable=false, updatable=false)
    private User liker;
}
