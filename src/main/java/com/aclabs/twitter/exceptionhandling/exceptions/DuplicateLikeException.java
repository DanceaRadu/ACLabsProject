package com.aclabs.twitter.exceptionhandling.exceptions;

public class DuplicateLikeException extends RuntimeException {
    public DuplicateLikeException() {
        super("Cannot like the same post twice");
    }
}
