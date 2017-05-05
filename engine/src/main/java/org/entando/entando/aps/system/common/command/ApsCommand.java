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
package org.entando.entando.aps.system.common.command;

import java.util.Date;

import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.context.ApsCommandContext;

/**
 * Class that applies a command on one or multiple items.
 * 
 * @author E.Mezzano
 */
public interface ApsCommand<C extends ApsCommandContext> {

	/**
	 * Returns the ID of the given command.
	 * @return The ID of the given command.
	 */
	public String getId();

	/**
	 * Returns the conventional name of the given command.
	 * @return The conventional name of the given command.
	 */
	public String getName();

	/**
	 * Init the context of the command.
	 * @param context The context of the command.
	 */
	public void init(C context);

	/**
	 * Apply the command.
	 */
	public void apply();

	/**
	 * Stop the command.
	 */
	public void stopCommand();

	/**
	 * Returns the status of the command.
	 * @return The status of the command.
	 */
	public ApsCommandStatus getStatus();
	
	/**
	 * Returns the instant of the end of the command.
	 * @return The instant of the end of the command.
	 */
	public Date getEndingTime();

	/**
	 * Returns true if the Command is ended (whether it has been completed or stopped early), false otherwise.
	 * @return true if the Command is ended (whether it has been completed or stopped early), false otherwise.
	 */
	public boolean isEnded();

}
