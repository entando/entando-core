package org.entando.entando.aps.system.exception;

public class CSRFProtectionException extends RuntimeException {

    public CSRFProtectionException(String message) {
        super(message);
    }
    public CSRFProtectionException(String message,Throwable e) {
        super(message,e);
    }
}
