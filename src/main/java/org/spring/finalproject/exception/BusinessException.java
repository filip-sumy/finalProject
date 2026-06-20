package org.spring.finalproject.exception;

public abstract class BusinessException extends RuntimeException {

    private final String messageKey;
    private final Object[] args;

    protected BusinessException(String messageKey, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args != null ? args : new Object[0];
    }

    public String getMessageKey() {
        return messageKey;
    }

    public Object[] getArgs() {
        return args;
    }
}
