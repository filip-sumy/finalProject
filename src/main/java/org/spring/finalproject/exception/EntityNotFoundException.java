package org.spring.finalproject.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
