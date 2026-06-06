package org.spring.finalproject.exception;

public class EntityInUseException extends RuntimeException {

    private final String messageKey;

    public EntityInUseException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
