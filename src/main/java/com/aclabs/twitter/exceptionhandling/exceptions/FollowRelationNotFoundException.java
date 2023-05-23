package com.aclabs.twitter.exceptionhandling.exceptions;

import java.util.UUID;

public class FollowRelationNotFoundException extends RuntimeException{

    public FollowRelationNotFoundException(UUID userID, UUID followedUserID) {
        super("There is no follow relation between follower with id: " + userID + " and user with id: " + followedUserID);
    }
}
