package com.pamela.helpdesk.resources.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandarError{
    private static final long serialVersionUID = 1L;

    private List<FildMessage> errors = new ArrayList<>();

    public ValidationError() {
    }

    public ValidationError(Long timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public List<FildMessage> getErrors() {
        return errors;
    }

    public void addErrors(String fieldName, String message) {
        this.errors.add(new FildMessage(fieldName,message));
    }
}
