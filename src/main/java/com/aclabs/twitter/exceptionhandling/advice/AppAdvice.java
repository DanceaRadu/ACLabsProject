package com.aclabs.twitter.exceptionhandling.advice;

import com.aclabs.twitter.exceptionhandling.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class AppAdvice extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> userNotFoundExceptionHandler(UserNotFoundException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("status", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(NoQueryResultException.class)
    public ResponseEntity<Object> userNoQueryResultExceptionHandler(NoQueryResultException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("status", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(FollowRelationNotFoundException.class)
    public ResponseEntity<Object> followRelationNotFoundExceptionHandler(FollowRelationNotFoundException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("status", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<Object> postNotFoundExceptionHandler(PostNotFoundException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("status", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<Object> likeNotFoundExceptionHandler(LikeNotFoundException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("status", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(InvalidFollowException.class)
    public ResponseEntity<Object> invalidFollowExceptionHandler(InvalidFollowException e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
