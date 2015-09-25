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

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "error")
@XmlType(propOrder = {"code", "message"})
public class ApiError implements Serializable {
	
	public ApiError() {}
	
	public ApiError(String code, String message) {
		this.setCode(code);
		this.setMessage(message);
	}
	
	public ApiError(String code, String message, Response.Status status) {
		this.setCode(code);
		this.setStatus(status);
		this.setMessage(message);
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}
	
	public String getMessage() {
		return _message;
	}
	public void setMessage(String message) {
		this._message = message;
	}
	
	public Status getStatus() {
		return _status;
	}
	protected void setStatus(Status status) {
		this._status = status;
	}
	
	private String _code;
	private String _message;
	
	private Response.Status _status;
	
}