package com.aclabs.twitter.exceptionhandling.exceptions;

import java.util.UUID;

public class LikeNotFoundException extends RuntimeException{
    public LikeNotFoundException(UUID userID, UUID postID) {
        super("User with id: " + userID + " has not liked the post with id: " + postID);
    }
}
