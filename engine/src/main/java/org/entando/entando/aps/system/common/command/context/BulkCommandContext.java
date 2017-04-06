package org.entando.entando.aps.system.common.command.context;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

public interface BulkCommandContext<I> extends ApsCommandContext {

	/**
	 * Returns the items on which to apply the command.
	 * @return The items on which to apply the command.
	 */
	public Collection<I> getItems();

	/**
	 * Returns the tracer of the command execution.
	 * @return The tracer of the command execution.
	 */
	public BulkCommandTracer<I> getTracer();

}
