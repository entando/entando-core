package org.entando.entando.aps.system.common.command.tracer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandWarningCode;

public class DefaultBulkCommandTracer<I> implements BulkCommandTracer<I> {

	@Override
	public void traceSuccess(I item) {
		// Do nothing
	}

	@Override
	public void traceWarning(I item, CommandWarningCode warningCode) {
		this._warnings.put(item, warningCode != null ? warningCode : CommandWarningCode.NOT_NECESSARY);
	}

	@Override
	public void traceError(I item, CommandErrorCode errorCode) {
		this._errors.put(item, errorCode != null ? errorCode : CommandErrorCode.ERROR);
	}

	@Override
	public List<I> getSuccesses() {
		return null;
	}

	@Override
	public Map<I, CommandWarningCode> getWarnings() {
		return _warnings;
	}

	@Override
	public Map<I, CommandErrorCode> getErrors() {
		return _errors;
	}

	private Map<I, CommandWarningCode> _warnings = new LinkedHashMap<I, CommandWarningCode>();

	private Map<I, CommandErrorCode> _errors = new LinkedHashMap<I, CommandErrorCode>();

}
