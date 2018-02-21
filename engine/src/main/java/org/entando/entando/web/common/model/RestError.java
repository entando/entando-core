package org.entando.entando.web.common.model;

public class RestError {

	private String code;
	private String message;

	public RestError() {
		//
	}

	public RestError(String code, String localizedErrorMessage) {
		this.setCode(code);
		this.setMessage(localizedErrorMessage);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
