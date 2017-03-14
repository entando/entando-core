package org.entando.entando.aps.system.common.command.report;

import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.constants.CommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.CommandStatus;
import org.entando.entando.aps.system.common.command.constants.CommandWarningCode;

public interface BulkCommandReport<I> {

	public int getTotal();

	public int getTotalPerformed();

	public int getPerformedSuccess();

	public int getPerformedErrors();

	public List<I> getSuccesses();

	public Map<I, CommandWarningCode> getWarnings();

	public Map<I, CommandErrorCode> getErrors();

	public CommandStatus getStatus();

}
