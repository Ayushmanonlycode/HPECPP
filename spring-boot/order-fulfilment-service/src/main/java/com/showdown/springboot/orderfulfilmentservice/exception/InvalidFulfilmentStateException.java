package com.showdown.springboot.orderfulfilmentservice.exception;

public class InvalidFulfilmentStateException extends RuntimeException {

    public InvalidFulfilmentStateException(String message) {
        super(message);
    }

    public InvalidFulfilmentStateException(String currentState, String targetState) {
        super("Cannot transition from " + currentState + " to " + targetState);
    }
}
