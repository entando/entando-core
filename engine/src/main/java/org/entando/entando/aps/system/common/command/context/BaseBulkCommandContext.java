/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
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
