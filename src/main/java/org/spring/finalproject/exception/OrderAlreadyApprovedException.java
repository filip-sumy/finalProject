package org.spring.finalproject.exception;

public class OrderAlreadyApprovedException extends BusinessException {

    public OrderAlreadyApprovedException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
