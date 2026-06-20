package org.spring.finalproject.exception;

public class EntityInUseException extends BusinessException {

    public EntityInUseException(String messageKey, Object... args) {
        super(messageKey, args);
    }
}
