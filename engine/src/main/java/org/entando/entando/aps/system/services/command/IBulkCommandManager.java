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
package org.entando.entando.aps.system.services.command;

import org.entando.entando.aps.system.common.command.BaseBulkCommand;
import org.entando.entando.aps.system.common.command.report.BulkCommandReport;

/**
 * Service responsible for the execution of bulk commands.<br/>
 * Its purpose is to run and cache commands. If a command is finished, is cached for a limited time, just to give the corresponding report.
 * 
 * @author E.Mezzano
 *
 */
public interface IBulkCommandManager {

	/**
	 * Add a command to the execution system.<br/>
	 * The command can be executed immediately or initiated by thread.
	 * @param owner The owner of the command (usually the code of the component running the command).
	 * @param command The command to be runned.
	 * @return The report of the command.
	 */
	public <I> BulkCommandReport<I> addCommand(String owner, BaseBulkCommand<I, ?, ?> command);

	/**
	 * Add a command to the execution system.<br/>
	 * The command can be executed immediately or initiated by thread.
	 * @param owner The owner of the command (usually the code of the component running the command).
	 * @param command The command to be runned.
	 * @param execInThread If true, the command is forced to run on a thread, if false, is runned immediately. 
	 * If null, the manager can decide how to run the command.
	 * @return The report of the command.
	 */
	public <I> BulkCommandReport<I> addCommand(String owner, BaseBulkCommand<I, ?, ?> command, Boolean execInThread);

	/**
	 * Returns the command.<br/> If null, the command can be expired and removed from cache.
	 * @param owner The owner of the command (usually the code of the component running the command).
	 * @param commandId The id of the command.
	 * @return The desired command.
	 */
	public BaseBulkCommand<?, ?, ?> getCommand(String owner, String commandId);

	/**
	 * Returns report of the command.<br/>
	 * If null, the command can be expired and removed from cache.
	 * @param owner The owner of the command (usually the code of the component running the command).
	 * @param commandId The id of the command.
	 * @return The report of the desired command.
	 */
	public BulkCommandReport<?> getCommandReport(String owner, String commandId);

	
	public void cleanCache();
}
