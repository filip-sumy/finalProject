package org.spring.finalproject.exception;

public class InsufficientStockException extends BusinessException {

    public InsufficientStockException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
