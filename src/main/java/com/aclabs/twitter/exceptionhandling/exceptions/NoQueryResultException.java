package com.aclabs.twitter.exceptionhandling.exceptions;

public class NoQueryResultException extends RuntimeException{
    public NoQueryResultException(String searchTerm) {
        super("The search using the query = '" + searchTerm + "' didn't return any results");
    }
}
