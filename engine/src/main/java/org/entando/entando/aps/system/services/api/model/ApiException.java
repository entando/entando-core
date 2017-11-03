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
	
	private final List<ApiError> _errors = new ArrayList<>();
	
}