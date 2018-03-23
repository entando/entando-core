package org.entando.entando.aps.system.exception;

import org.springframework.validation.BindingResult;

public class RestRourceNotFoundException extends RuntimeException {

    private String errorCode;
    private String objectName;
    private String objectCode;
    private BindingResult bindingResult;

    public RestRourceNotFoundException(String objectName, String objectCode) {
        this("1", objectName, objectCode);
    }

    public RestRourceNotFoundException(String errorCode, String objectName, String objectCode) {
        this.errorCode = errorCode;
        this.objectName = objectName;
        this.objectCode = objectCode;
    }

    public RestRourceNotFoundException(String errorCode, String message, String objectName, String objectCode) {
        super(message);
        this.errorCode = errorCode;
        this.objectName = objectName;
        this.objectCode = objectCode;
    }

    public RestRourceNotFoundException(BindingResult bindingResult) {
        this.setBindingResult(bindingResult);
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public BindingResult getBindingResult() {
        return bindingResult;
    }

    public void setBindingResult(BindingResult bindingResult) {
        this.bindingResult = bindingResult;
    }

}
