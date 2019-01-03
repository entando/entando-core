package com.agiletec.aps.system.common;

public class EntandoDbException extends RuntimeException {

    public EntandoDbException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
}
