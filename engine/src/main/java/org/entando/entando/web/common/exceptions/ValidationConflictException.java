package org.entando.entando.web.common.exceptions;

import org.springframework.validation.BindingResult;

public class ValidationConflictException extends RuntimeException {

	private final BindingResult bindingResult;

	public ValidationConflictException(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

}
