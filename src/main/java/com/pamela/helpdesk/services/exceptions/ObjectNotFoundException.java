package com.pamela.helpdesk.services.exceptions;

public class ObjectNotFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ObjectNotFoundException(final String message) {
        super(message);
    }

    public ObjectNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
