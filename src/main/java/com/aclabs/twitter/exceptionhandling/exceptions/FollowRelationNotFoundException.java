package com.aclabs.twitter.exceptionhandling.exceptions;

public class FollowRelationNotFoundException extends RuntimeException{

    public FollowRelationNotFoundException(Long userID, Long followedUserID) {
        super("There is no follow relation between follower with id: " + userID + " and user with id: " + followedUserID);
    }
}
