package org.entando.entando.aps.system.common.command.tracer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.ApsCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandWarningCode;

/**
 * The default bulk {@link ApsCommand} tracer.
 * Its do not trace successes, only errors and warnings.
 * 
 * @author E.Mezzano
 *
 * @param <I> The type of items on which to apply the command.
 */
public class DefaultBulkCommandTracer<I> implements BulkCommandTracer<I> {

	/**
	 * This implementation do not trace successes.
	 * @param item The item onto track the success. In this case is ignored.
	 */
	@Override
	public void traceSuccess(I item) {
		// Do nothing
	}

	@Override
	public void traceWarning(I item, ApsCommandWarningCode warningCode) {
		this._warnings.put(item, warningCode != null ? warningCode : ApsCommandWarningCode.NOT_NECESSARY);
	}

	@Override
	public void traceError(I item, ApsCommandErrorCode errorCode) {
		this._errors.put(item, errorCode != null ? errorCode : ApsCommandErrorCode.ERROR);
	}

	@Override
	public List<I> getSuccesses() {
		return null;
	}

	@Override
	public Map<I, ApsCommandWarningCode> getWarnings() {
		return _warnings;
	}

	@Override
	public Map<I, ApsCommandErrorCode> getErrors() {
		return _errors;
	}

	private Map<I, ApsCommandWarningCode> _warnings = new LinkedHashMap<I, ApsCommandWarningCode>();

	private Map<I, ApsCommandErrorCode> _errors = new LinkedHashMap<I, ApsCommandErrorCode>();

}
