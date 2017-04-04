package org.entando.entando.aps.system.common.command.tracer;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.ApsCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandWarningCode;

/**
 * Trace a bulk {@link ApsCommand} execution.
 * 
 * @author E.Mezzano
 *
 * @param <I> The type of items on which to apply the command.
 */
public interface BulkCommandTracer<I> {

	/**
	 * Trace a success.
	 * @param item The item onto track the success.
	 */
	public void traceSuccess(I item);

	/**
	 * Trace a warning, with its warning code.
	 * @param item The item onto track the warning.
	 * @param warningCode The warning code.
	 */
	public void traceWarning(I item, ApsCommandWarningCode warningCode);

	/**
	 * Trace an error, with its error code.
	 * @param item The item onto track the error.
	 * @param errorCode The error code.
	 */
	public void traceError(I item, ApsCommandErrorCode errorCode);

	/**
	 * Returns the traced succeeded items.
	 * @return The traced succeeded items.
	 */
	public List<I> getSuccesses();

	/**
	 * Returns the traced items with warnings.
	 * @return The traced items with warnings.
	 */
	public Map<I, ApsCommandWarningCode> getWarnings();

	/**
	 * Returns the traced items with errors.
	 * @return The traced items with errors.
	 */
	public Map<I, ApsCommandErrorCode> getErrors();

}
