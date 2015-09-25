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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author E.Santoboni
 */
public abstract class AbstractApiResponse implements Serializable {
    
    public void setResult(Object result, String html) {
        AbstractApiResponseResult responseResult = this.createResponseResultInstance();
        responseResult.setMainResult(result);
        responseResult.setHtml(html);
        this.setResult(responseResult);
    }
    
    protected abstract AbstractApiResponseResult createResponseResultInstance();
    
    @XmlElement(name = "error", required = true)
    @XmlElementWrapper(name = "errors")
    public List<ApiError> getErrors() {
        return this._errors;
    }
    
    public void addError(ApiError error) {
        if (null != error) {
			this._errors.add(error);
        }
    }
    
    public void addErrors(List<ApiError> errors) {
        if (null == errors) {
            return;
        }
		this.getErrors().addAll(errors);
    }
    
    public Object getResult() {
        return _result;
    }
    protected void setResult(Object result) {
        this._result = result;
    }
    
    private List<ApiError> _errors = new ArrayList<ApiError>();
    private Object _result;
	
}