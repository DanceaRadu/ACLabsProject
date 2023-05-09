package com.aclabs.twitter.exceptionhandling.exceptions;

public class LikeNotFoundException extends RuntimeException{
    public LikeNotFoundException(Long userID, Long postID) {
        super("User with id: " + userID + " has not liked the post with id: " + postID);
    }
}
