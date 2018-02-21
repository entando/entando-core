package org.entando.entando.web.common.model;

import java.util.ArrayList;
import java.util.List;

public class RestResponse {

	private Object payload;
	private List<RestError> errors = new ArrayList<>();
	private Object metadata;

	public RestResponse() {
		//
	}

	public RestResponse(Object payload) {
		this.payload = payload;
	}

	public RestResponse(Object payload, List<RestError> errors, Object metadata) {
		this.payload = payload;
		this.errors = errors;
		this.metadata = metadata;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}

	public Object getMetadata() {
		return metadata;
	}
	public void setMetadata(Object metadata) {
		this.metadata = metadata;
	}

	public List<RestError> getErrors() {
		return errors;
	}

	public void setErrors(List<RestError> errors) {
		this.errors = errors;
	}

	public void addErrors(List<RestError> errors) {
        this.errors.addAll(errors);

	}

}
