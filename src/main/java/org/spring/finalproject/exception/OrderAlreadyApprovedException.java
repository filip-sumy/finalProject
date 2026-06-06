package org.spring.finalproject.exception;

public class OrderAlreadyApprovedException extends RuntimeException {

    public OrderAlreadyApprovedException(String message) {
        super(message);
    }
}
