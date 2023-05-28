package com.aclabs.twitter.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "likes")
public @Data class Like {

    @Embeddable
    public static class LikeID implements Serializable{

        @Column(name = "liker_id", nullable = false)
        private UUID likerID;
        @Column(name = "post_id", nullable = false)
        private UUID postID;

        public LikeID() {
        }

        public LikeID(UUID likerID, UUID postID) {
            this.likerID = likerID;
            this.postID = postID;
        }

        public UUID getLikerID() {
            return likerID; }
        public void setLikerID(UUID likerID) {
            this.likerID= likerID;
        }

        public UUID getPostID() {
            return postID;
        }

        public void setPostID(UUID postID) {
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
            return likerID.hashCode() + postID.hashCode();
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
