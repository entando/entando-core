/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.exception;

import org.springframework.validation.BindingResult;

public class ResourceNotFoundException extends RuntimeException {

    private String errorCode;
    private String objectName;
    private String objectCode;
    private BindingResult bindingResult;

    public ResourceNotFoundException(String objectName, String objectCode) {
        this("1", objectName, objectCode);
    }

    public ResourceNotFoundException(String errorCode, String objectName, String objectCode) {
        this.errorCode = errorCode;
        this.objectName = objectName;
        this.objectCode = objectCode;
    }

    public ResourceNotFoundException(String errorCode, String message, String objectName, String objectCode) {
        super(message);
        this.errorCode = errorCode;
        this.objectName = objectName;
        this.objectCode = objectCode;
    }

    public ResourceNotFoundException(BindingResult bindingResult) {
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
