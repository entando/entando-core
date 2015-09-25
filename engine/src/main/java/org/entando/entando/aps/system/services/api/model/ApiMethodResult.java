/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author E.Santoboni
 */
public final class ApiMethodResult implements Serializable {
    
    public Object getResult() {
        return _result;
    }
    public void setResult(Object result) {
        this._result = result;
    }
    
    public void addError(String errorCode, String description) {
        ApiError error = new ApiError(errorCode, description);
        this.addError(error);
    }
    
    public void addError(ApiError error) {
        if (null == this.getErrors()) {
            this.setErrors(new ArrayList<ApiError>());
        }
        this.getErrors().add(error);
    }
    
    public List<ApiError> getErrors() {
        return _errors;
    }
    public void setErrors(List<ApiError> errors) {
        this._errors = errors;
    }
    
    private Object _result;
    private List<ApiError> _errors;
    
}
