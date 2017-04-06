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
package org.entando.entando.aps.system.common.command.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.entando.entando.aps.system.common.command.ApsCommand;
import org.entando.entando.aps.system.common.command.constants.ApsCommandErrorCode;
import org.entando.entando.aps.system.common.command.constants.ApsCommandStatus;
import org.entando.entando.aps.system.common.command.constants.ApsCommandWarningCode;

/**
 * The report of a bulk {@link ApsCommand}
 * @author E.Mezzano
 *
 * @param <I> The type of items on which the command is applied.
 */
public interface BulkCommandReport<I> {
	
	/**
	 * The ID of the given command. Can coincide with the thread name.
	 * @return The ID of the given command.
	 */
	public String getCommandId();

	/**
	 * Returns the number items onto apply the command.
	 * @return The number items onto apply the command.
	 */
	public int getTotal();

	/**
	 * Returns the number items onto the command is applied.
	 * @return The number items onto the command is applied.
	 */
	public int getApplyTotal();

	/**
	 * Returns the number items onto the command is succesfully applied.
	 * @return The number items onto the command is succesfully applied.
	 */
	public int getApplySuccesses();

	/**
	 * Returns the number items onto the command is applied with errors.
	 * @return The number items onto the command is applied with errors.
	 */
	public int getApplyErrors();

	/**
	 * Returns the succeeded items of the command.
	 * @return The succeeded items of the command.
	 */
	public List<I> getSuccesses();

	/**
	 * Returns the items with warnings of the command.
	 * @return The items with warnings of the command.
	 */
	public Map<I, ApsCommandWarningCode> getWarnings();

	/**
	 * Returns the items with errors of the command.
	 * @return The items with errors of the command.
	 */
	public Map<I, ApsCommandErrorCode> getErrors();

	/**
	 * Returns the status of the command
	 * @return The status of the command
	 */
	public ApsCommandStatus getStatus();

	/**
	 * Returns the instant of the end of the command.
	 * @return The instant of the end of the command.
	 */
	public Date getEndingTime();

}
