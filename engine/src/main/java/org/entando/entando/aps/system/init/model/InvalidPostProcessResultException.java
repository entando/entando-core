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
package org.entando.entando.aps.system.init.model;

import org.entando.entando.aps.system.services.api.model.ApiMethod;

/**
 * @author E.Santoboni
 */
public class InvalidPostProcessResultException extends Exception {

	public InvalidPostProcessResultException(int resultCode, int expectedCode, String path, ApiMethod.HttpMethod method) {
		this.setExpectedCode(expectedCode);
		this.setResultCode(resultCode);
		this.setPath(path);
		this.setMethod(method);
	}

	@Override
	public String getMessage() {
		return this.getMethod() + " " + this.getPath() + " - Invalid result " + this.getResultCode() + " - expected " + this.getExpectedCode();
	}

	public String getPath() {
		return _path;
	}
	public void setPath(String path) {
		this._path = path;
	}

	public ApiMethod.HttpMethod getMethod() {
		return _method;
	}
	public void setMethod(ApiMethod.HttpMethod method) {
		this._method = method;
	}

	public int getResultCode() {
		return _resultCode;
	}
	protected void setResultCode(int resultCode) {
		this._resultCode = resultCode;
	}

	public int getExpectedCode() {
		return _expectedCode;
	}
	protected void setExpectedCode(int expectedCode) {
		this._expectedCode = expectedCode;
	}

	private String _path;
	private ApiMethod.HttpMethod _method;
	private int _resultCode;
	private int _expectedCode;

}