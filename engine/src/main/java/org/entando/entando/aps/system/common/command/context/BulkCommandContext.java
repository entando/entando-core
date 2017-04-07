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
