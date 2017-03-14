package org.entando.entando.aps.system.common.command.tracer;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandWarningCode;

public interface BulkCommandTracer<I> {

	public void traceSuccess(I item);

	public void traceWarning(I item, CommandWarningCode warningCode);

	public void traceError(I item, CommandErrorCode errorCode);

	public List<I> getSuccesses();

	public Map<I, CommandWarningCode> getWarnings();

	public Map<I, CommandErrorCode> getErrors();

}
