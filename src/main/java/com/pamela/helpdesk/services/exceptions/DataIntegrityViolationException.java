package com.pamela.helpdesk.services.exceptions;

public class DataIntegrityViolationException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DataIntegrityViolationException(final String message) {
        super(message);
    }

    public DataIntegrityViolationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
