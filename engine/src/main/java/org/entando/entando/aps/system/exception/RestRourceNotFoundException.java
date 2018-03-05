package org.entando.entando.aps.system.exception;


public class RestRourceNotFoundException extends RuntimeException {

    private String objectName;
    private String objectCode;

    public RestRourceNotFoundException(String objectName, String objectCode) {
        this.objectName = objectName;
        this.objectCode = objectCode;
    }

    public RestRourceNotFoundException(String message, String objectName, String objectCode) {
        super(message);
        this.objectName = objectName;
        this.objectCode = objectCode;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectCode() {
        return objectCode;
    }

    public void setObjectCode(String objectCode) {
        this.objectCode = objectCode;
    }
}
