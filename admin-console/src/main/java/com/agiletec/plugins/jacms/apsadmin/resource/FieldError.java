package com.agiletec.plugins.jacms.apsadmin.resource;

// Immutable class containing field errors
public final class FieldError {

    public final String fieldName;
    public final String errorMessage;

    FieldError(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
}
