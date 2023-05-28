package com.aclabs.twitter.exceptionhandling.exceptions;

public class InvalidFollowException extends RuntimeException {
    public InvalidFollowException() {
        super("A user cannot follow themselves");
    }
}
