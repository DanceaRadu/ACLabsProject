package com.aclabs.twitter.exceptionhandling.exceptions;

import java.util.UUID;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(UUID postID) {
        super("Post with id: " + postID + " could not be found");
    }
}
