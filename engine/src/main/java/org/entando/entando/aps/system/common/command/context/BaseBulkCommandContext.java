package org.entando.entando.aps.system.common.command.context;

import java.util.Collection;

import org.entando.entando.aps.system.common.command.tracer.BulkCommandTracer;

public class BaseBulkCommandContext<I> implements BulkCommandContext<I> {
	
	public BaseBulkCommandContext(Collection<I> items, BulkCommandTracer<I> tracer) {
		this.setItems(items);
		this.setTracer(tracer);
	}

	@Override
	public Collection<I> getItems() {
		return _items;
	}
	/**
	 * Sets the items on which to apply the command.
	 * @param items The items on which to apply the command.
	 */
	public void setItems(Collection<I> items) {
		this._items = items;
	}

	@Override
	public BulkCommandTracer<I> getTracer() {
		return _tracer;
	}
	/**
	 * Sets the tracer of the command execution.
	 * @param tracer The tracer of the command execution.
	 */
	public void setTracer(BulkCommandTracer<I> tracer) {
		this._tracer = tracer;
	}

	private Collection<I> _items;
	private BulkCommandTracer<I> _tracer;

}
