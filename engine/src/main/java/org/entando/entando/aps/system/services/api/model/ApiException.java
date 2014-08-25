/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.aps.system.services.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

/**
 * @author E.Santoboni
 */
public class ApiException extends Exception {
	
	public ApiException(ApiError error) {
		super(error.getMessage());
		this.getErrors().add(error);
	}
	
	public ApiException(ApiError error, Throwable cause) {
		super(cause);
		this.getErrors().add(error);
	}
	
	public ApiException(List<ApiError> errors, Throwable cause) {
		super(cause);
		this.getErrors().addAll(errors);
	}
	
	public ApiException(List<ApiError> errors) {
		super();
		this.getErrors().addAll(errors);
	}
	
	public ApiException(String errorKey, String message, Throwable cause) {
		super(message, cause);
		this.addError(errorKey);
	}
	
	public ApiException(String errorKey, String message) {
		super(message);
		this.addError(errorKey);
	}
	
	public ApiException(String errorKey, String message, Response.Status status) {
		super(message);
		this.addError(errorKey, status);
	}
	
	public ApiException(String errorKey, Throwable cause) {
		super(cause);
		this.addError(errorKey);
	}
	
	protected void addError(String key) {
		this.getErrors().add(new ApiError(key, getMessage()));
	}
	
	protected void addError(String key, Response.Status status) {
		this.getErrors().add(new ApiError(key, getMessage(), status));
	}
	
	public List<ApiError> getErrors() {
		return this._errors;
	}
	
	private List<ApiError> _errors = new ArrayList<ApiError>();
	
}