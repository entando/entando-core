package org.entando.entando.aps.system.exception;


public class RestServerError extends RuntimeException {

    private Exception exception;

    public RestServerError(String message, Exception ex) {
        super(message);
        this.exception = ex;
    }

    public RestServerError(String message, Throwable t) {
        super(message, t);
    }
}
