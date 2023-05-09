package com.aclabs.twitter.exceptionhandling.exceptions;

public class PostNotFoundException extends RuntimeException{
    public PostNotFoundException(Long postID) {
        super("Post with id: " + postID + " could not be found");
    }
}
