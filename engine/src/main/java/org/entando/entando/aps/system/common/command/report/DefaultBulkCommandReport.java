package org.entando.entando.aps.system.common.command.report;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandStatus;
import org.entando.entando.aps.system.common.command.constants.CommandWarningCode;


public class DefaultBulkCommandReport<I> implements BulkCommandReport<I> {

	public DefaultBulkCommandReport(BaseBulkCommand<I, ?> command) {
		this._command = command;
	}

	@Override
	public int getTotal() {
		return this._command.getTotal();
	}

	@Override
	public int getTotalPerformed() {
		return this._command.getPerformedSuccess() + this._command.getPerformedErrors();
	}

	@Override
	public int getPerformedSuccess() {
		return this._command.getPerformedSuccess();
	}

	@Override
	public int getPerformedErrors() {
		return this._command.getPerformedErrors();
	}

	@Override
	public List<I> getSuccesses() {
		return this._command.getTracer().getSuccesses();
	}

	@Override
	public Map<I, CommandWarningCode> getWarnings() {
		return this._command.getTracer().getWarnings();
	}

	@Override
	public Map<I, CommandErrorCode> getErrors() {
		return this._command.getTracer().getErrors();
	}

	@Override
	public CommandStatus getStatus() {
		return this._command.getStatus();
	}

	private BaseBulkCommand<I, ?> _command;

}
