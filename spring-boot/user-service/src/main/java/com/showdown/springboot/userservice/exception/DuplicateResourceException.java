package com.showdown.springboot.userservice.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String field, String value) {
        super("A user with " + field + " '" + value + "' already exists");
    }
}
