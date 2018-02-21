package org.entando.entando.web.common.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationGenericException extends RuntimeException {

	private final BindingResult bindingResult;

	public ValidationGenericException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

}
